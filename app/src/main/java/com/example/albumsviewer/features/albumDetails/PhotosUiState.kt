package com.example.albumsviewer.features.albumDetails

import androidx.paging.PagingData
import com.example.albumsviewer.data.models.view.Photo
import com.example.albumsviewer.result.UiState

sealed class PhotosUiState : UiState() {
    data class PhotosLoaded(val data: PagingData<Photo>) : PhotosUiState()
    data object OnPhotosLoadedSuccessfully : PhotosUiState()
    data object OnEmptyPhotosList : PhotosUiState()
}
