package com.lovegame.data.repositories

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.lovegame.BuildConfig
import com.lovegame.R
import com.lovegame.data.mapper.UserMapper
import com.lovegame.domain.model.UserData
import com.lovegame.domain.repositories.UserRepository
import com.lovegame.domain.util.Resource
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.cancellation.CancellationException

class UserRepositoryImpl(
    private val oneTapClient: SignInClient,
    private val userMapper: UserMapper,
    private val auth: FirebaseAuth,
    private val context: Context
) : UserRepository {

    val TAG =  context.getString(R.string.app_name) + "Tag "+ "UserRepositoryImpl"
    override suspend fun getUser(): Resource<UserData> {
        val userData = userMapper.responseToUserData(auth.currentUser)
        return responseToResource(userData)
    }

    override suspend fun signInGoogle(): IntentSender? {
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
            Log.d(TAG + "Rep", e.toString())
            null
        }
    }

    override suspend fun createUserWithCredentials(
        email: String,
        password: String
    ): Resource<UserData> {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            user?.sendEmailVerification()
            val userData = userMapper.responseToUserData(user)
            responseToResource(userData)
        } catch (e: Exception) {
            Log.d(TAG + "Rep", e.toString())
            when (e) {
                is FirebaseAuthUserCollisionException -> responseToResource(
                    null, context.getString(
                        R.string.user_already_exists
                    )
                )

                else -> responseToResource(null, context.getString(R.string.error_creating_acount))
            }
        }
    }

    override fun sendEmailVerification() {
        val user = auth.currentUser
        try {
            user?.sendEmailVerification()
            Log.d(TAG + "Rep", "Email verification sent")
        } catch (e: Exception) {
            Log.d(TAG + "Rep", e.toString())
        }
    }

    override suspend fun signInWithCredentials(
        email: String,
        password: String
    ): Resource<UserData> {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            val userData = userMapper.responseToUserData(user)
            responseToResource(userData)
        } catch (e: Exception) {
            Log.d(TAG + "Rep", e.toString())
            when (e) {
                is FirebaseAuthInvalidCredentialsException -> responseToResource(
                    null,
                    context.getString(
                        R.string.email_or_password_is_incorrect
                    )
                )

                is IllegalArgumentException -> responseToResource(
                    null,
                    context.getString(R.string.email_or_password_is_empty)
                )

                else -> responseToResource(null, context.getString(R.string.log_in_error))
            }
        }
    }

    override fun signOut() {
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

    private fun responseToResource(response: UserData?, errmsg: String = ""): Resource<UserData> {
        if (response != null) {
            return Resource.Success(response)
        }
        return Resource.Error(errmsg)
    }
}