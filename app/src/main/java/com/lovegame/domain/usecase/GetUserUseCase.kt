package com.lovegame.domain.usecase

import com.lovegame.domain.model.UserData
import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserUseCase(private val userRepository: UserRepository) {
    suspend fun execute(): Flow<Resource<UserData>> = flow {
        emit(userRepository.getUser())
    }
}