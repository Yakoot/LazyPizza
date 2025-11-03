package dev.mamkin.lazypizza.auth.presentation.signin

import dev.mamkin.lazypizza.auth.presentation.signin.components.otp.OtpState

data class SignInState(
    val phoneNumber: String = "",
    val isPhoneSubmitEnabled: Boolean = false,
    val isCodeSubmitEnabled: Boolean = false,
    val isResendEnabled: Boolean = false,
    val isOtpFieldVisible: Boolean = false,
    val isCodeSent: Boolean = false,
    val resendText: String = "",
    val otpState: OtpState = OtpState()
)
