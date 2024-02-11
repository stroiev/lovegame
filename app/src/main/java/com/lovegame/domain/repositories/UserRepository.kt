package com.lovegame.domain.repositories

import android.content.Intent
import android.content.IntentSender
import com.lovegame.domain.model.UserData
import com.lovegame.domain.util.Resource

interface UserRepository {
    suspend fun getUser(): Resource<UserData>
    suspend fun signInGoogle(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): UserData?
    suspend fun createUserWithCredentials(email: String, password: String): Resource<UserData>
    fun sendEmailVerification()
    suspend fun signInWithCredentials(email: String, password: String): Resource<UserData>
    fun signOut()
}