package com.example.albumsviewer.data.dataSource

import com.example.albumsviewer.data.api.API
import javax.inject.Inject

interface IRemoteDataSource {
    val api: API
}


class RemoteDataSource @Inject constructor(
    override val api: API
) : IRemoteDataSource