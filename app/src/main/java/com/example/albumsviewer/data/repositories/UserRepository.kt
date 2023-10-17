package com.example.albumsviewer.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.albumsviewer.data.dataSource.IRemoteDataSource
import com.example.albumsviewer.data.models.network.AlbumAPI
import com.example.albumsviewer.data.models.network.UserAPI
import com.example.albumsviewer.data.models.view.Photo
import com.example.albumsviewer.data.pagingSource.PhotosPagingSource
import com.example.albumsviewer.di.IoDispatcher
import com.example.albumsviewer.utils.NETWORK_PAGE_SIZE
import com.example.albumsviewer.utils.NetworkUtils
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val networkUtils: NetworkUtils,
    @IoDispatcher
    private val ioDispatcher: CoroutineDispatcher,
    private val remoteDataSource: IRemoteDataSource
) : IUserRepository {

    override suspend fun getUsersList(): Flow<List<UserAPI>> = flow {
        try {
            if (!networkUtils.isConnectedToNetwork())
                throw IOException("Please Check your Internet connection")

            emit(
                with(remoteDataSource.api.getUsersList()) {
                    if (isSuccessful) body() ?: listOf()
                    else throw IOException("Something went wrong, Please try again later")
                }
            )

        } catch (throwable: Throwable) {
            throw throwable
        }
    }.flowOn(ioDispatcher)

    override suspend fun getUserAlbums(userId: Int): Flow<List<AlbumAPI>> = flow {
        try {
            if (!networkUtils.isConnectedToNetwork())
                throw IOException("Please Check your Internet connection")

            emit(
                with(remoteDataSource.api.getUserAlbums(userId)) {
                    if (isSuccessful) body() ?: listOf()
                    else throw IOException("Something went wrong, Please try again later")
                }
            )

        } catch (throwable: Throwable) {
            throw throwable
        }
    }.flowOn(ioDispatcher)

    override suspend fun getAlbumPhotos(
        albumId: Int,
        searchQuery: String?
    ): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PhotosPagingSource(
                   remoteDataSource = remoteDataSource,
                    albumId= albumId,
                    searchQuery = searchQuery
                )
            }
        ).flow
    }
}