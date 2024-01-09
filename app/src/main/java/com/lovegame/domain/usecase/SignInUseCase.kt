package com.lovegame.domain.usecase

import android.content.IntentSender
import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInUseCase(private val userRepository: UserRepository) {
    suspend fun execute(): Flow<Resource<IntentSender>> = flow {
        emit(responseToResource(userRepository.signIn()))
    }

    private fun responseToResource(response:IntentSender?):Resource<IntentSender>{
        if(response != null){
                return Resource.Success(response)

        }
        return Resource.Error("user not logged in")
    }
}