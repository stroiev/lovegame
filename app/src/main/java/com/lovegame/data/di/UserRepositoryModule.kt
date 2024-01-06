package com.lovegame.data.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.lovegame.data.mapper.UserMapper
import com.lovegame.data.repositories.UserRepositoryImpl
import com.lovegame.domain.repositories.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserRepositoryModule {
    @Singleton
    @Provides
    fun provideNewsRepository(
        oneTapClient: SignInClient,
        userMapper: UserMapper
    ): UserRepository {
        return UserRepositoryImpl(
            oneTapClient,
            userMapper
        )
    }
    @Singleton
    @Provides
    fun provideSignInClient(@ApplicationContext appContext: Context): SignInClient {
        return Identity.getSignInClient(appContext)
    }
}