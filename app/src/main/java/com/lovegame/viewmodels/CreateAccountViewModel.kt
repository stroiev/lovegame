package com.lovegame.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
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