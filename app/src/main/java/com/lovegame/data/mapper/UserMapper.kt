package com.lovegame.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.lovegame.domain.model.UserData

class UserMapper {
    fun responseToUserData(firebaseuser: FirebaseUser?): UserData? {
        return firebaseuser?.run {
            UserData(
                userId = uid,
                username = displayName,
                profilePictureUrl = photoUrl?.toString(),
                isVerified = isEmailVerified
            )
        }
    }
}