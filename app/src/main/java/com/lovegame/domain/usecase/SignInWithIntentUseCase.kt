package com.lovegame.domain.usecase

import android.content.Intent
import com.lovegame.domain.model.UserData
import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.util.Resource

class SignInWithIntentUseCase(private val userRepository: UserRepository) {
//    suspend fun execute(): Flow<Resource<UserData>> = flow {
//        emit(responseToResource(userRepository.getUser()))
//    }
    suspend fun execute(intent: Intent): Resource<UserData> = responseToResource(userRepository.signInWithIntent(intent))

    private fun responseToResource(response:UserData?):Resource<UserData>{
        if(response != null){
                return Resource.Success(response)

        }
        return Resource.Error("log in cancelled")
    }
}