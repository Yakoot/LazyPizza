package dev.mamkin.lazypizza.auth.presentation.signin.components.otp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class OtpViewModel : ViewModel() {
    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    fun onAction(action: OtpAction) {
        when (action) {
            is OtpAction.OnChangeFieldFocused -> onChangeFieldFocused(action.index)
            is OtpAction.OnEnterNumber -> onEnterNumber(action.number, action.index)
            OtpAction.OnKeyboardBack -> onKeyboardBack()
        }
    }

    private fun onChangeFieldFocused(index: Int) {
        val currentCode = state.value.code
        val firstEmptyFieldIndex = currentCode.indexOfFirst { it == null }

        val focusedIndex = if (currentCode[index] == null && firstEmptyFieldIndex != -1) {
            firstEmptyFieldIndex
        } else {
            index
        }

        _state.update {
            it.copy(
                focusedIndex = focusedIndex,
                focusChangeId = it.focusChangeId + 1
            )
        }
    }

    private fun onEnterNumber(number: Int?, index: Int) {
        val newCode = state.value.code.mapIndexed { currentIndex, currentNumber ->
            if (currentIndex == index) {
                number
            } else {
                currentNumber
            }
        }

        val wasNumberRemoved = number == null

        val newFocusedIndex = if (wasNumberRemoved || state.value.code.getOrNull(index) != null) {
            state.value.focusedIndex
        } else {
            getNextFocusedTextFieldIndex(
                currentCode = state.value.code,
                currentFocusedIndex = state.value.focusedIndex
            )
        }

        val focusChanged = newFocusedIndex != state.value.focusedIndex

        _state.update {
            it.copy(
                code = newCode,
                focusedIndex = newFocusedIndex,
                focusChangeId = if (focusChanged) it.focusChangeId + 1 else it.focusChangeId,
                isValid = if (newCode.none { it == null }) {
                    newCode.joinToString("") == "1234"
                } else {
                    null
                }
            )
        }
    }

    private fun onKeyboardBack() {
        val previousIndex = getPreviousFocusedIndex(state.value.focusedIndex)
        _state.update {
            it.copy(
                code = it.code.mapIndexed { currentIndex, currentNumber ->
                    if (currentIndex == previousIndex) {
                        null
                    } else {
                        currentNumber
                    }
                },
                focusedIndex = previousIndex,
                focusChangeId = it.focusChangeId + 1
            )
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
}