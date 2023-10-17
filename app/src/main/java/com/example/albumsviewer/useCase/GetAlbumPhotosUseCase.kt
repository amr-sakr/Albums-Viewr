package com.example.albumsviewer.useCase

import com.example.albumsviewer.data.repositories.IUserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class GetAlbumPhotosUseCase @Inject constructor(
    private val repository: IUserRepository
) {
    suspend operator fun invoke(
        albumId: Int,
        searchQuery: String?
    ) = repository.getAlbumPhotos(albumId, searchQuery)
}