package dev.mamkin.lazypizza.auth.presentation.signin

import android.app.Activity

sealed interface SignInAction {
    data class OnPhoneNumberChanged(val phoneNumber: String) : SignInAction
    data class OnPhoneNumberSubmitted(val activity: Activity) : SignInAction
    data class OnOtpEnterNumber(val number: Int?, val index: Int) : SignInAction
    data class OnOtpChangeFieldFocused(val index: Int) : SignInAction
    data object OnOtpKeyboardBack : SignInAction
    data object OnResendClicked : SignInAction
    data object OnSignInClicked : SignInAction
    data object OnContinueWithoutSignInClicked : SignInAction
    data object OnCodeSubmitted : SignInAction

}
