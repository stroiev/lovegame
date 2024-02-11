package com.lovegame.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovegame.domain.model.UserData
import com.lovegame.domain.usecase.CreateUserWithCredentialsUseCase
import com.lovegame.domain.usecase.SignOutUseCase
import com.lovegame.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateAccountViewModel @Inject constructor(
    private val createUserWithCredentialsUseCase: CreateUserWithCredentialsUseCase,
    private val signOutUseCase: SignOutUseCase
) : ViewModel() {

    private val _resourceUserData = MutableStateFlow<Resource<UserData>>(Resource.Empty())
    val resourceUserData = _resourceUserData.asStateFlow()
    fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex()
        return email.matches(emailRegex)
    }

    fun isValidPasswordLenght(password: String): Boolean {
        return (password.length in 8..20)
    }

    fun doPasswordsMatch(password: String, retype: String): Boolean {
        return (password == retype)
    }

    fun createUserWithCredentials(email: String, password: String) = viewModelScope.launch {
        _resourceUserData.value = Resource.Loading()
        createUserWithCredentialsUseCase.execute(email, password).collect {
            _resourceUserData.value = it
        }
    }

    fun signOut() = signOutUseCase.execute()

    fun resetUserDataResource() {
        _resourceUserData.value = Resource.Empty()
    }
}