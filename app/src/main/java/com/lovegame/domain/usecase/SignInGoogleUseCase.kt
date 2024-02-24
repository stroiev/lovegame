package com.lovegame.domain.usecase

import android.content.IntentSender
import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInGoogleUseCase(private val userRepository: UserRepository) {
    suspend fun execute(): Flow<Resource<IntentSender>> = flow {
        emit(userRepository.signInGoogle())
    }
}