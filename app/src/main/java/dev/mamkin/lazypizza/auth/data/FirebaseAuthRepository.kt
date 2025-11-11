package dev.mamkin.lazypizza.auth.data

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dev.mamkin.lazypizza.auth.domain.AuthException
import dev.mamkin.lazypizza.auth.domain.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAuthRepository(
    private val auth: FirebaseAuth
) : AuthRepository {

    override suspend fun sendVerificationCode(
        phoneNumber: String,
        activity: Activity
    ): Result<String> {
        return try {
            val verificationId = suspendCancellableCoroutine { continuation ->
                val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Auto-verification succeeded
                        // This callback will be invoked in two situations:
                        // 1 - Instant verification
                        // 2 - Auto-retrieval on some devices
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        val exception = when (e) {
                            is FirebaseAuthInvalidCredentialsException -> AuthException.InvalidPhoneNumber()
                            is FirebaseTooManyRequestsException -> AuthException.TooManyRequests()
                            else -> AuthException.NetworkError()
                        }
                        continuation.resumeWithException(exception)
                    }

                    override fun onCodeSent(
                        verificationId: String,
                        token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        continuation.resume(verificationId)
                    }
                }

                val options = PhoneAuthOptions.newBuilder(auth)
                    .setPhoneNumber(phoneNumber)
                    .setTimeout(60L, TimeUnit.SECONDS)
                    .setActivity(activity)
                    .setCallbacks(callbacks)
                    .build()

                PhoneAuthProvider.verifyPhoneNumber(options)
            }

            Result.success(verificationId)
        } catch (e: AuthException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(AuthException.UnknownError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun verifyCode(verificationId: String, code: String): Result<Unit> {
        return try {
            suspendCancellableCoroutine { continuation ->
                val credential = PhoneAuthProvider.getCredential(verificationId, code)

                auth.signInWithCredential(credential)
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener { e ->
                        val exception = when (e) {
                            is FirebaseAuthInvalidCredentialsException -> AuthException.InvalidVerificationCode()
                            else -> AuthException.UnknownError(e.message ?: "Unknown error")
                        }
                        continuation.resumeWithException(exception)
                    }
            }

            Result.success(Unit)
        } catch (e: AuthException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(AuthException.UnknownError(e.message ?: "Unknown error"))
        }
    }

    override suspend fun signOut() {
        auth.signOut()
    }

    override fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override fun observeAuthState(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            trySend(firebaseAuth.currentUser != null)
        }

        auth.addAuthStateListener(authStateListener)

        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
}