package com.lovegame.data.repositories

import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.lovegame.BuildConfig
import com.lovegame.data.mapper.UserMapper
import com.lovegame.domain.model.UserData
import com.lovegame.domain.repositories.UserRepository
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class UserRepositoryImpl(
    private val oneTapClient: SignInClient,
    private val userMapper: UserMapper,
    private val auth: FirebaseAuth,
) : UserRepository {

    override suspend fun getUser(): UserData? = userMapper.responseToUserData(auth.currentUser)

    override suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signInWithIntent(intent: Intent): UserData? {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            userMapper.responseToUserData(user)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun signOut() {
        try {
            oneTapClient.signOut()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.GOOGLE_ID_TOKEN)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}