package com.lovegame.domain.repositories

import android.content.Intent
import android.content.IntentSender
import com.lovegame.domain.model.UserData

interface UserRepository {
    suspend fun getUser(): UserData?
    suspend fun signIn(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): UserData?
    fun signOut()
}