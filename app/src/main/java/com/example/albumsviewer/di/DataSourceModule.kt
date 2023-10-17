package com.example.albumsviewer.di


import com.example.albumsviewer.data.api.API
import com.example.albumsviewer.data.dataSource.IRemoteDataSource
import com.example.albumsviewer.data.dataSource.RemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataSourceModule {

    @Provides
    @Singleton
    fun bindIRemoteDataSource(
        api: API
    ): IRemoteDataSource {
        return RemoteDataSource(api)
    }

}
