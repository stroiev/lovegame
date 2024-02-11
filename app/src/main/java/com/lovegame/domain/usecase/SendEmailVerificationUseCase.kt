package com.lovegame.domain.usecase

import com.lovegame.domain.repositories.UserRepository

class SendEmailVerificationUseCase(private val userRepository: UserRepository) {
    fun execute() = userRepository.sendEmailVerification()
}