package dev.mamkin.lazypizza.auth.domain

import android.app.Activity

interface AuthRepository {
    suspend fun sendVerificationCode(phoneNumber: String, activity: Activity): Result<String>
    suspend fun verifyCode(verificationId: String, code: String): Result<Unit>
    suspend fun signOut()
    fun isUserSignedIn(): Boolean
    fun getCurrentUserId(): String?
}