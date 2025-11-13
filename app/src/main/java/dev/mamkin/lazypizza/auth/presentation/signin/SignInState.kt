package dev.mamkin.lazypizza.auth.presentation.signin

data class SignInState(
    val phoneNumber: String = "",
    val isPhoneSubmitEnabled: Boolean = false,
    val isCodeSubmitEnabled: Boolean = false,
    val isResendEnabled: Boolean = false,
    val isOtpFieldVisible: Boolean = false,
    val isCodeSent: Boolean = false,
    val codeError: Boolean = false,
    val resendCountdownTimer: String = "",
    val isLoading: Boolean = false,
)
