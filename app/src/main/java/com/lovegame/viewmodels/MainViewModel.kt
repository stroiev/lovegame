package com.lovegame.viewmodels

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovegame.data.Session
import com.lovegame.domain.model.UserData
import com.lovegame.domain.usecase.GetUserUseCase
import com.lovegame.domain.usecase.SignInUseCase
import com.lovegame.domain.usecase.SignInWithIntentUseCase
import com.lovegame.domain.usecase.SignOutUseCase
import com.lovegame.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel  @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val signInUseCase: SignInUseCase,
    private val signInWithIntentUseCase: SignInWithIntentUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _resourceUserData = MutableStateFlow<Resource<UserData>>(Resource.Empty())
    val resourceUserData = _resourceUserData.asStateFlow()

    private val _resourceIntentSender = MutableStateFlow<Resource<IntentSender>>(Resource.Empty())
    val resourceIntentSender = _resourceIntentSender.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000)
            _isLoading.value = false
        }
    }
    fun getUser() {
        viewModelScope.launch {
            _resourceUserData.value = Resource.Loading()
            getUserUseCase.execute().collect{
                storeSessionData(it.data)
                _resourceUserData.value = it
            }
        }
    }
    fun signIn() = viewModelScope.launch {
        _resourceIntentSender.value = Resource.Loading()
        signInUseCase.execute().collect{
            _resourceIntentSender.value = it
        }
    }

    fun signInWithIntent(intent: Intent) = viewModelScope.launch {
        _resourceUserData.value = Resource.Loading()
        signInWithIntentUseCase.execute(intent).collect{
            storeSessionData(it.data)
            _resourceUserData.value = it
        }
    }

    fun storeSessionData(userData: UserData?) {
        Session.USERDATA = userData
    }

    fun signOut() = signOutUseCase.execute()


    fun resetUserDataResource() {
        _resourceUserData.value = Resource.Empty()
    }

    fun resetIntentSenderResource() {
        _resourceIntentSender.value = Resource.Empty()
    }
}