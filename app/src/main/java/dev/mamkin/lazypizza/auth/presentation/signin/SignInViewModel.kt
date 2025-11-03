package dev.mamkin.lazypizza.auth.presentation.signin

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.lazypizza.auth.domain.AuthRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SignInViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val phoneRegex = "^\\+[0-9]{10,13}$".toRegex()

    private val _event = Channel<SignInEvent>()
    val event = _event.receiveAsFlow()

    private val _state = MutableStateFlow(SignInState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = SignInState()
        )

    fun onAction(action: SignInAction) {
        when (action) {
            SignInAction.OnContinueWithoutSignInClicked -> {
                viewModelScope.launch {
                    _event.send(SignInEvent.BackToHome)
                }
            }

            is SignInAction.OnOtpChangeFieldFocused -> TODO()
            is SignInAction.OnOtpEnterNumber -> TODO()
            SignInAction.OnOtpKeyboardBack -> TODO()
            is SignInAction.OnPhoneNumberChanged -> {
                _state.update {
                    it.copy(
                        phoneNumber = action.phoneNumber,
                        isPhoneSubmitEnabled = isValidPhoneNumber(action.phoneNumber)
                    )
                }
            }

            is SignInAction.OnPhoneNumberSubmitted -> onPhoneNumberSubmit(action.activity)
            SignInAction.OnResendClicked -> TODO()
            SignInAction.OnSignInClicked -> TODO()
            SignInAction.OnCodeSubmitted -> TODO()
        }
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneRegex.matches(phoneNumber)
    }

    private fun onPhoneNumberSubmit(activity: Activity) {
        val phoneNumber = _state.value.phoneNumber
        viewModelScope.launch {
            authRepository.sendVerificationCode(phoneNumber, activity)
                .onSuccess { println("!!!!! $it") }
        }
    }

}
