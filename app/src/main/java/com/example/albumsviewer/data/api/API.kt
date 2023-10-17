package com.example.albumsviewer.data.api

import com.example.albumsviewer.data.models.network.AlbumsResponse
import com.example.albumsviewer.data.models.network.PhotosResponse
import com.example.albumsviewer.data.models.network.UsersResponse
import com.example.albumsviewer.utils.PATH_ALBUMS
import com.example.albumsviewer.utils.PATH_PHOTOS
import com.example.albumsviewer.utils.PATH_USERS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface API {

    @GET(PATH_USERS)
    suspend fun getUsersList(): Response<UsersResponse>

    @GET(PATH_ALBUMS)
    suspend fun getUserAlbums(
        @Query("userId") userId: Int,
    ): Response<AlbumsResponse>


    @GET(PATH_PHOTOS)
    suspend fun getAlbumPhotos(
        @Query("(albumId") albumId: Int,
        @Query("_page") page: Int,
        @Query("_limit") limit: Int,
        @Query("q") searchQuery: String?,
    ): Response<PhotosResponse>
}