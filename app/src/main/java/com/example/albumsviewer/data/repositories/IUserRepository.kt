package com.example.albumsviewer.data.repositories

import androidx.paging.PagingData
import com.example.albumsviewer.data.models.network.AlbumAPI
import com.example.albumsviewer.data.models.network.UserAPI
import com.example.albumsviewer.data.models.view.Photo
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getUsersList(): Flow<List<UserAPI>>

    suspend fun getUserAlbums(userId: Int): Flow<List<AlbumAPI>>

    suspend fun getAlbumPhotos(
        albumId: Int,
        searchQuery: String?
    ): Flow<PagingData<Photo>>
}