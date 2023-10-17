package com.example.albumsviewer.data.pagingSource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.albumsviewer.data.dataSource.IRemoteDataSource
import com.example.albumsviewer.data.models.view.Photo
import com.example.albumsviewer.data.models.view.toPhoto
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE = 1

class PhotosPagingSource(
    private val remoteDataSource: IRemoteDataSource,
    private val albumId: Int,
    private val searchQuery: String?
) : PagingSource<Int, Photo>() {
    override fun getRefreshKey(state: PagingState<Int, Photo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Photo> {
        val page = params.key ?: STARTING_PAGE

        return try {
            val response = remoteDataSource.api.getAlbumPhotos(
                albumId = albumId,
                page = page,
                limit = params.loadSize / 3,
                searchQuery = searchQuery,
            )
            val photos = response.body()?.map {
                it.toPhoto()
            } ?: listOf()
            val nextKey = if (photos.isEmpty()) null else page + 1
            LoadResult.Page(
                data = photos,
                prevKey = if (page == STARTING_PAGE) null else page,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}

