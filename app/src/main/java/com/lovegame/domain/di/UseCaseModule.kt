package com.lovegame.domain.di

import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.usecase.CreateUserWithCredentialsUseCase
import com.lovegame.domain.usecase.GetUserUseCase
import com.lovegame.domain.usecase.ResetPasswordUseCase
import com.lovegame.domain.usecase.SendEmailVerificationUseCase
import com.lovegame.domain.usecase.SignInGoogleUseCase
import com.lovegame.domain.usecase.SignInWithCredentialsUseCase
import com.lovegame.domain.usecase.SignInWithIntentUseCase
import com.lovegame.domain.usecase.SignOutUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideGetUserUseCase(userRepository: UserRepository): GetUserUseCase {
        return GetUserUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideSignInGoogleUseCase(userRepository: UserRepository): SignInGoogleUseCase {
        return SignInGoogleUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideSignInWithIntentUseCase(userRepository: UserRepository): SignInWithIntentUseCase {
        return SignInWithIntentUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideCreateUserWithCredentialsUseCase(userRepository: UserRepository): CreateUserWithCredentialsUseCase {
        return CreateUserWithCredentialsUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideSendEmailVerificationUseCase(userRepository: UserRepository): SendEmailVerificationUseCase {
        return SendEmailVerificationUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideResetPasswordUseCase(userRepository: UserRepository): ResetPasswordUseCase {
        return ResetPasswordUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideSignInWithCredentialsUseCase(userRepository: UserRepository): SignInWithCredentialsUseCase {
        return SignInWithCredentialsUseCase(userRepository)
    }

    @Singleton
    @Provides
    fun provideSignOutUseCase(userRepository: UserRepository): SignOutUseCase {
        return SignOutUseCase(userRepository)
    }
}