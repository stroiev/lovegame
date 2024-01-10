package com.lovegame.domain.usecase

import com.lovegame.domain.repositories.UserRepository

class SignOutUseCase(private val userRepository: UserRepository) {
    fun execute() = userRepository.signOut()
}