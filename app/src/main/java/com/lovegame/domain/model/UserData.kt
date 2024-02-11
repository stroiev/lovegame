package com.lovegame.domain.model

data class UserData (
    val userId: String,
    val username: String?,
    val profilePictureUrl: String?,
    val isVerified: Boolean = false
)