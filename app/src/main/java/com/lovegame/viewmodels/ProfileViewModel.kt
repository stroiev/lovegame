package com.lovegame.viewmodels

import androidx.lifecycle.ViewModel
import com.lovegame.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
) : ViewModel() {

    fun signOut() = signOutUseCase.execute()
}