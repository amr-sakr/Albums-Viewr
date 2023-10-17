package com.example.albumsviewer.features.profile

import com.example.albumsviewer.data.models.view.Album
import com.example.albumsviewer.data.models.view.User
import com.example.albumsviewer.result.UiState

sealed class UserProfileUiState : UiState() {
    data class UserInfoLoaded(val data: User) : UserProfileUiState()
    data class UserAlbumsLoaded(val data: List<Album>) : UserProfileUiState()
}
