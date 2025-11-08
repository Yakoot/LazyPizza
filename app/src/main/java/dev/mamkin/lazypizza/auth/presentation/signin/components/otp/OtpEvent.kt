package dev.mamkin.lazypizza.auth.presentation.signin.components.otp

sealed interface OtpEvent {
    data class CodeChanged(val code: String) : OtpEvent
}
