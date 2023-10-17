package com.example.albumsviewer.useCase

import com.example.albumsviewer.data.models.view.toAlbum
import com.example.albumsviewer.data.repositories.IUserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ViewModelScoped
class GetAlbumsUseCase @Inject constructor(
    private val repository: IUserRepository
) {

    suspend operator fun invoke(userId: Int) =
        repository.getUserAlbums(userId).map { albums ->
            albums.map { album ->
                album.toAlbum()
            }
        }

}