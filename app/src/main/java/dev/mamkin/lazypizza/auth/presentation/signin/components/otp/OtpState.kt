package dev.mamkin.lazypizza.auth.presentation.signin.components.otp

import androidx.compose.runtime.Stable

@Stable
data class OtpState(
    val code: List<Int?> = (1..DIGITS_COUNT).map { null },
    val focusedIndex: Int? = null,
    val isValid: Boolean? = null,
    val focusChangeId: Long = 0L
)