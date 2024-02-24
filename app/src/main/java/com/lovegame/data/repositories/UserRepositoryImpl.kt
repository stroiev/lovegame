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

    val TAG = context.getString(R.string.app_name) + "Tag " + "UserRepositoryImpl"
    override suspend fun getUser(): Resource<UserData> {
        val userData = userMapper.responseToUserData(auth.currentUser)
        return userData?.let { Resource.Success(it) } ?: Resource.Error("")
    }

    override suspend fun signInGoogle(): Resource<IntentSender> {
        return try {
            val result = oneTapClient.beginSignIn(buildSignInRequest()).await()
            Resource.Success(result.pendingIntent.intentSender)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Resource.Error(context.getString(R.string.user_not_logged_in))
        }
    }

    override suspend fun signInWithIntent(intent: Intent): Resource<UserData> {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            val userData = userMapper.responseToUserData(user)
            userData?.let { Resource.Success(it) } ?: Resource.Error("")
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Resource.Error(context.getString(R.string.log_in_cancelled))
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
            userData?.let { Resource.Success(it) } ?: Resource.Error("")
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            when (e) {
                is FirebaseAuthUserCollisionException ->
                    Resource.Error(context.getString(R.string.user_already_exists))

                else -> Resource.Error(context.getString(R.string.error_creating_acount))
            }
        }
    }

    override fun sendEmailVerification() {
        val user = auth.currentUser
        try {
            user?.sendEmailVerification()
            Log.d(TAG, "Email verification sent")
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    override suspend fun resetPassword(email: String): Resource<UserData> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Resource.Empty()
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Resource.Error(context.getString(R.string.faild_to_send_reset_password))
        }
    }

    override suspend fun signInWithCredentials(
        email: String,
        password: String
    ): Resource<UserData> {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            val userData = userMapper.responseToUserData(user)
            userData?.let { Resource.Success(it) } ?: Resource.Error("")
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            when (e) {
                is FirebaseAuthInvalidCredentialsException ->
                    Resource.Error(context.getString(R.string.email_or_password_is_incorrect))

                is IllegalArgumentException ->
                    Resource.Error(context.getString(R.string.email_or_password_is_empty))

                else -> Resource.Error(context.getString(R.string.log_in_error))
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

}