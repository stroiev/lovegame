package com.lovegame

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

    fun signIn() = viewModelScope.launch {
        _resourceIntentSender.value = Resource.Loading()
        _resourceIntentSender.value = signInUseCase.execute()
    }

    fun signInWithIntent(intent: Intent) = viewModelScope.launch {
        _resourceUserData.value = Resource.Loading()
        val resource = signInWithIntentUseCase.execute(intent)
        storeSessionData(resource.data)
        _resourceUserData.value = resource
    }

    fun getUser() = viewModelScope.launch {
        _resourceUserData.value = Resource.Loading()
        val resource = getUserUseCase.execute()
        storeSessionData(resource.data)
        _resourceUserData.value = resource
    }

    fun storeSessionData(userData: UserData?) {
        Session.USERDATA = userData
    }

    suspend fun signOut() = signOutUseCase.execute()


    fun resetUserDataResource() {
        _resourceUserData.value = Resource.Empty()
    }

//    fun getUser() = liveData {
//        getUserUseCase.execute().collect{
//            emit(it)
//        }
//    }
}