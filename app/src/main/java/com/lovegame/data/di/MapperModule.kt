package com.lovegame.data.di

import com.lovegame.data.mapper.UserMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    @Singleton
    @Provides
    fun providesUserMapper(): UserMapper {
        return UserMapper()
    }
}