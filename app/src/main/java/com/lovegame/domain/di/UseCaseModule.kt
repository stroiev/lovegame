package com.lovegame.domain.di

import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.usecase.GetUserUseCase
import com.lovegame.domain.usecase.SignInUseCase
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
    fun provideGetUserUseCase(userRepository: UserRepository):GetUserUseCase{
        return GetUserUseCase(userRepository)
    }
    @Singleton
    @Provides
    fun provideSignInUseCase(userRepository: UserRepository): SignInUseCase {
        return SignInUseCase(userRepository)
    }
    @Singleton
    @Provides
    fun provideSignInWithIntentUseCase(userRepository: UserRepository): SignInWithIntentUseCase {
        return SignInWithIntentUseCase(userRepository)
    }
    @Singleton
    @Provides
    fun provideSignOutUseCase(userRepository: UserRepository): SignOutUseCase {
        return SignOutUseCase(userRepository)
    }
}