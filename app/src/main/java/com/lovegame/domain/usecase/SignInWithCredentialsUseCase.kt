package com.lovegame.domain.usecase

import com.lovegame.domain.model.UserData
import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SignInWithCredentialsUseCase(private val userRepository: UserRepository) {
    suspend fun execute(email: String, password: String): Flow<Resource<UserData>> = flow {
        emit(responseToResource(userRepository.signInWithCredentials(email, password)))
    }
    private fun responseToResource(response:UserData?):Resource<UserData>{
        if(response != null){
                return Resource.Success(response)

        }
        return Resource.Error("log in cancelled")
    }
}