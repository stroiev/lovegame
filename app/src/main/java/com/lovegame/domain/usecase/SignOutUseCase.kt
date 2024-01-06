package com.lovegame.domain.usecase

import com.lovegame.domain.repositories.UserRepository

class SignOutUseCase(private val userRepository: UserRepository) {
//    suspend fun execute(): Flow<Resource<UserData>> = flow {
//        emit(responseToResource(userRepository.getUser()))
//    }
    suspend fun execute() = userRepository.signOut()
}