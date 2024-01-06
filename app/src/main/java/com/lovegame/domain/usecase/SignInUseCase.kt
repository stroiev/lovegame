package com.lovegame.domain.usecase

import android.content.IntentSender
import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.util.Resource

class SignInUseCase(private val userRepository: UserRepository) {
//    suspend fun execute(): Flow<Resource<UserData>> = flow {
//        emit(responseToResource(userRepository.getUser()))
//    }
    suspend fun execute(): Resource<IntentSender> = responseToResource(userRepository.signIn())

    private fun responseToResource(response:IntentSender?):Resource<IntentSender>{
        if(response != null){
                return Resource.Success(response)

        }
        return Resource.Error("user not logged in")
    }
}