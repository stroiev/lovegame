package com.lovegame.viewmodels

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovegame.data.Session
import com.lovegame.domain.model.UserData
import com.lovegame.domain.usecase.GetUserUseCase
import com.lovegame.domain.usecase.ResetPasswordUseCase
import com.lovegame.domain.usecase.SendEmailVerificationUseCase
import com.lovegame.domain.usecase.SignInGoogleUseCase
import com.lovegame.domain.usecase.SignInWithCredentialsUseCase
import com.lovegame.domain.usecase.SignInWithIntentUseCase
import com.lovegame.domain.usecase.SignOutUseCase
import com.lovegame.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val signInGoogleUseCase: SignInGoogleUseCase,
    private val signInWithIntentUseCase: SignInWithIntentUseCase,
    private val signInWithCredentialsUseCase: SignInWithCredentialsUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val sendEmailVerificationUseCase: SendEmailVerificationUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _resourceUserData = MutableStateFlow<Resource<UserData>>(Resource.Empty())
    val resourceUserData = _resourceUserData.asStateFlow()

    private val _resourceIntentSender = MutableStateFlow<Resource<IntentSender>>(Resource.Empty())
    val resourceIntentSender = _resourceIntentSender.asStateFlow()

    fun getUser() = viewModelScope.launch {
        _resourceUserData.value = Resource.Loading()
        getUserUseCase.execute().collect {
            storeSessionData(it.data)
            _resourceUserData.value = it
        }
    }

    fun signInGoogle() = viewModelScope.launch {
        _resourceIntentSender.value = Resource.Loading()
        signInGoogleUseCase.execute().collect {
            _resourceIntentSender.value = it
        }
    }

    fun signInWithIntent(intent: Intent) = viewModelScope.launch {
        _resourceUserData.value = Resource.Loading()
        signInWithIntentUseCase.execute(intent).collect {
            storeSessionData(it.data)
            _resourceUserData.value = it
        }
    }

    fun signInWithCredentials(email: String, password: String) = viewModelScope.launch {
        _resourceUserData.value = Resource.Loading()
        signInWithCredentialsUseCase.execute(email, password).collect {
            storeSessionData(it.data)
            _resourceUserData.value = it
        }
    }

    fun storeSessionData(userData: UserData?) {
        Session.USERDATA = userData
    }

    fun sendEmailVerification() {
        sendEmailVerificationUseCase.execute()
    }

    fun resetPassword(email: String) = viewModelScope.launch {
        _resourceUserData.value = Resource.Loading()
        resetPasswordUseCase.execute(email).collect {
            _resourceUserData.value = it
        }
    }

    fun signOut() = signOutUseCase.execute()


    fun resetUserDataResource() {
        _resourceUserData.value = Resource.Empty()
    }

    fun resetIntentSenderResource() {
        _resourceIntentSender.value = Resource.Empty()
    }
}