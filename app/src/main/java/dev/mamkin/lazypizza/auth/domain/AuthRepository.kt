package dev.mamkin.lazypizza.auth.domain

import android.app.Activity
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun sendVerificationCode(phoneNumber: String, activity: Activity): Result<String>
    suspend fun verifyCode(verificationId: String, code: String): Result<Unit>
    suspend fun signOut()
    fun isUserSignedIn(): Boolean
    fun getCurrentUserId(): String?
    fun observeAuthState(): Flow<Boolean>
}