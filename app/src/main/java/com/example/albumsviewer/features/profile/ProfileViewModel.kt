package com.example.albumsviewer.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.albumsviewer.di.MainDispatcher
import com.example.albumsviewer.result.UiState
import com.example.albumsviewer.useCase.GetAlbumsUseCase
import com.example.albumsviewer.useCase.GetUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @MainDispatcher
    private val mainDispatcher: CoroutineDispatcher,
    private val userUseCase: GetUserUseCase,
    private val albumsUseCase: GetAlbumsUseCase
) : ViewModel() {

    private val _userState = MutableStateFlow<UiState>(UiState.Initial)
    val userState = _userState.asStateFlow()

    private val _albumsState = MutableStateFlow<UiState>(UiState.Initial)
    val albumsState = _albumsState.asStateFlow()

    private val _profileError = Channel<UiState>()
    val profileError = _profileError.receiveAsFlow()

    private val handler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _profileError.send(UiState.Error(throwable.message ?: "Something went wrong"))
        }
    }

    init {
        getUserProfile()
    }

    private fun getUserProfile() {
        viewModelScope.launch(handler + mainDispatcher) {
            _userState.emit(UiState.Loading)
            userUseCase().collect { user ->
                getAlbums(user.id)
                _userState.emit(UserProfileUiState.UserInfoLoaded(user))
            }
        }
    }

    private fun getAlbums(userId: Int) {
        viewModelScope.launch(handler + mainDispatcher) {
            _albumsState.emit(UiState.Loading)
            albumsUseCase(userId).collect { albums ->
                _albumsState.emit(UserProfileUiState.UserAlbumsLoaded(albums))
            }
        }
    }

}

