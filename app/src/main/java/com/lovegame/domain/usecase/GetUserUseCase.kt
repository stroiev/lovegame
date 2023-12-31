package com.lovegame.domain.usecase

import com.lovegame.domain.model.UserData
import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.util.Resource

class GetUserUseCase(private val userRepository: UserRepository) {
//    suspend fun execute(): Flow<Resource<UserData>> = flow {
//        emit(responseToResource(userRepository.getUser()))
//    }
    suspend fun execute(): Resource<UserData> = responseToResource(userRepository.getUser())

    private fun responseToResource(response:UserData?):Resource<UserData>{
        if(response != null){
                return Resource.Success(response)

        }
        return Resource.Error("user not logged in")
    }
}