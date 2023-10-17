package com.example.albumsviewer.features.albumDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.cachedIn
import com.example.albumsviewer.di.MainDispatcher
import com.example.albumsviewer.result.UiState
import com.example.albumsviewer.useCase.GetAlbumPhotosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
    private val getAlbumPhotosUseCase: GetAlbumPhotosUseCase
) : ViewModel() {

    private val albumId = savedStateHandle.get<Int>("album_id") ?: -1

    private val _photosState = MutableStateFlow<UiState>(UiState.Initial)
    val photosState = _photosState.asStateFlow()

    private val _photosError = Channel<UiState>()
    val photosError = _photosError.receiveAsFlow()

    private val searchQuery = MutableStateFlow<String?>(null)

    private val handler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _photosError.send(UiState.Error(throwable.message ?: "Something went wrong"))
        }
    }

    init {
        getAlbumPhotos(albumId)
    }

    private fun getAlbumPhotos(albumId: Int, search: String? = null) {
        viewModelScope.launch(handler + mainDispatcher) {
            searchQuery.value = search
            _photosState.emit(UiState.Loading)
            val photos = searchQuery.flatMapLatest {query->
                getAlbumPhotosUseCase(albumId, query).cachedIn(viewModelScope)
            }
            _photosState.emit(PhotosUiState.PhotosLoaded(photos.stateIn(viewModelScope).value))
        }
    }

    fun onPagingStateChanged(loadState: CombinedLoadStates, adapterCurrentItemCount: Int) {
        viewModelScope.launch {
            when (loadState.refresh) {
                is LoadState.Loading -> _photosState.emit(UiState.Loading)
                is LoadState.Error -> {
                    _photosError.send(
                        UiState.Error(
                            (loadState.refresh as LoadState.Error).error.message
                                ?: "Something went wrong"
                        )
                    )
                }

                is LoadState.NotLoading -> {
                    if (adapterCurrentItemCount > 0) {
                        _photosState.emit(PhotosUiState.OnPhotosLoadedSuccessfully)
                    } else if (loadState.append.endOfPaginationReached) {
                        _photosState.emit(PhotosUiState.OnEmptyPhotosList)
                    }
                }
            }
        }
    }

    fun searchPhotos(query: String?) {
        getAlbumPhotos(albumId , query)
    }
}