package com.lovegame.domain.usecase

import android.content.Intent
import com.lovegame.domain.model.UserData
import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInWithIntentUseCase(private val userRepository: UserRepository) {
    suspend fun execute(intent: Intent): Flow<Resource<UserData>> = flow {
        emit(userRepository.signInWithIntent(intent))
    }
}