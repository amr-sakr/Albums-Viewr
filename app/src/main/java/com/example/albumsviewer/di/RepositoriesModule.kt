package com.example.albumsviewer.di


import com.example.albumsviewer.data.repositories.IUserRepository
import com.example.albumsviewer.data.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoriesModule {

    @Singleton
    @Provides
    fun provideUserRepository(repository: UserRepository): IUserRepository = repository
}
