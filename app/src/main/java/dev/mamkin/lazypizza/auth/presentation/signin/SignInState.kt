package dev.mamkin.lazypizza.auth.presentation.signin

data class SignInState(
    val phoneNumber: String = "",
    val isPhoneSubmitEnabled: Boolean = false,
    val isCodeSubmitEnabled: Boolean = false,
    val isResendEnabled: Boolean = false,
    val isOtpFieldVisible: Boolean = true,
    val isCodeSent: Boolean = true,
    val resendText: String = "",
)
