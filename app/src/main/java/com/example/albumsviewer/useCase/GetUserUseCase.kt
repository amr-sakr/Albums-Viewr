package com.example.albumsviewer.useCase

import com.example.albumsviewer.data.models.view.User
import com.example.albumsviewer.data.models.view.toUser
import com.example.albumsviewer.data.repositories.IUserRepository
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@ViewModelScoped
class GetUserUseCase @Inject constructor(private val repository: IUserRepository) {

    suspend operator fun invoke(): Flow<User> = repository.getUsersList().flatMapConcat { users ->
        flow {
            val randomUserId = (1..users.size).random()
            val randomUser = users.find { it.id == randomUserId }
            if (randomUser != null) {
                emit(randomUser.toUser())
            }
        }
    }
}