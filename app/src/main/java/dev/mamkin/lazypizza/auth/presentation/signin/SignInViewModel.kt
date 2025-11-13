package dev.mamkin.lazypizza.auth.presentation.signin

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.mamkin.lazypizza.auth.domain.AuthException
import dev.mamkin.lazypizza.auth.domain.AuthRepository
import dev.mamkin.lazypizza.auth.presentation.signin.components.otp.DIGITS_COUNT
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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

    private var verificationId: String? = null
    private var currentCode: String = ""

    private var resendCountdownJob: Job? = null

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
            is SignInAction.OnPhoneNumberChanged -> {
                _state.update {
                    it.copy(
                        phoneNumber = action.phoneNumber,
                        isPhoneSubmitEnabled = isValidPhoneNumber(action.phoneNumber)
                    )
                }
            }

            is SignInAction.OnPhoneNumberSubmitted -> onPhoneNumberSubmit(action.activity)
            is SignInAction.OnResendClicked -> onResendClicked(action.activity)
            SignInAction.OnCodeSubmitted -> onCodeSubmited()
            is SignInAction.OnCodeChanged -> onCodeChanged(action.code)
        }
    }

    private fun onCodeSubmited() {
        viewModelScope.launch {
            verificationId?.let {
                _state.update { it.copy(isLoading = true) }
                authRepository.verifyCode(it, currentCode)
                    .onSuccess {
                        _state.update { it.copy(isLoading = false) }
                        _event.send(SignInEvent.BackToHome)
                    }
                    .onFailure { throwable ->
                        _state.update { it.copy(isLoading = false) }
                        when (throwable as? AuthException) {
                            is AuthException.InvalidVerificationCode -> {
                                _state.update {
                                    it.copy(
                                        codeError = true
                                    )
                                }
                            }

                            else -> {
                                _event.send(
                                    SignInEvent.SnackbarError(
                                        throwable.message ?: "Unknown error"
                                    )
                                )
                            }
                        }
                    }
            }
        }
    }

    private fun onCodeChanged(code: String) {
        currentCode = code
        _state.update {
            it.copy(
                isCodeSubmitEnabled = code.length == DIGITS_COUNT
            )
        }
    }

    fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneRegex.matches(phoneNumber)
    }

    private fun onPhoneNumberSubmit(activity: Activity) {
        val phoneNumber = _state.value.phoneNumber
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authRepository.sendVerificationCode(phoneNumber, activity)
                .onSuccess {
                    verificationId = it

                    _state.update {
                        it.copy(
                            isCodeSent = true,
                            isOtpFieldVisible = true,
                            isLoading = false
                        )
                    }

                    startResendCountdown()
                }
                .onFailure { throwable ->
                    _state.update { it.copy(isLoading = false) }
                    _event.send(SignInEvent.SnackbarError(throwable.message ?: "Unknown error"))
                }
        }
    }

    private fun onResendClicked(activity: Activity) {
        val phoneNumber = _state.value.phoneNumber
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            authRepository.sendVerificationCode(phoneNumber, activity)
                .onSuccess {
                    verificationId = it
                    _state.update { it.copy(isLoading = false) }
                    startResendCountdown()
                }
                .onFailure { throwable ->
                    _state.update { it.copy(isLoading = false) }
                    _event.send(SignInEvent.SnackbarError(throwable.message ?: "Unknown error"))
                }
        }
    }

    private fun startResendCountdown() {
        resendCountdownJob?.cancel()

        resendCountdownJob = viewModelScope.launch {
            val totalSeconds = 60

            for (secondsRemaining in totalSeconds downTo 0) {
                val minutes = secondsRemaining / 60
                val seconds = secondsRemaining % 60
                val timeText = String.format("%02d:%02d", minutes, seconds)

                _state.update {
                    it.copy(
                        resendCountdownTimer = timeText,
                        isResendEnabled = secondsRemaining == 0
                    )
                }

                if (secondsRemaining > 0) {
                    delay(1000L)
                }
            }

            _state.update {
                it.copy(
                    resendCountdownTimer = ""
                )
            }
        }
    }

    fun getPreviousFocusedIndex(currentIndex: Int?): Int? {
        return currentIndex?.minus(1)?.coerceAtLeast(0)
    }

    private fun getNextFocusedTextFieldIndex(
        currentCode: List<Int?>,
        currentFocusedIndex: Int?
    ): Int? {
        if (currentFocusedIndex == null) {
            return null
        }

        if (currentFocusedIndex == DIGITS_COUNT - 1) {
            return currentFocusedIndex
        }

        return getFirstEmptyFieldIndexAfterFocusedIndex(
            code = currentCode,
            currentFocusedIndex = currentFocusedIndex
        )
    }


    private fun getFirstEmptyFieldIndexAfterFocusedIndex(
        code: List<Int?>,
        currentFocusedIndex: Int
    ): Int {
        code.forEachIndexed { index, number ->
            if (index <= currentFocusedIndex) {
                return@forEachIndexed
            }
            if (number == null) {
                return index
            }
        }
        return currentFocusedIndex
    }

    fun clear() {
        _state.update { SignInState() }
    }

}
