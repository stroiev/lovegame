package com.lovegame.viewmodels

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lovegame.data.Session
import com.lovegame.domain.model.UserData
import com.lovegame.domain.usecase.GetUserUseCase
import com.lovegame.domain.usecase.SignInGoogleUseCase
import com.lovegame.domain.usecase.SignInWithCredentialsUseCase
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
class CreateAccountViewModel  @Inject constructor() : ViewModel() {
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
}