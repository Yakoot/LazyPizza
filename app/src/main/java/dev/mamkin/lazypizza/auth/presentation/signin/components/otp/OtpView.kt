package dev.mamkin.lazypizza.auth.presentation.signin.components.otp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.mamkin.lazypizza.app.di.appModule
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.KoinApplicationPreview

@Composable
fun OtpView(
    modifier: Modifier = Modifier,
    state: OtpState,
    focusRequesters: List<FocusRequester>,
    onAction: (OtpAction) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.code.forEachIndexed { index, number ->
            OtpInputField(
                modifier = Modifier.weight(1f),
                number = number,
                focusRequester = focusRequesters[index],
                onFocusChanged = { isFocused ->
                    if (isFocused) {
                        onAction(OtpAction.OnChangeFieldFocused(index))
                    }
                },
                onNumberChanged = { newNumber ->
                    onAction(OtpAction.OnEnterNumber(newNumber, index))
                },
                onKeyboardBack = {
                    onAction(OtpAction.OnKeyboardBack)
                }
            )
        }
    }
}

data class OtpState(
    val code: List<Int?> = (1..4).map { null },
    val focusedIndex: Int? = null,
    val isValid: Boolean? = null
)

sealed interface OtpAction {
    data class OnEnterNumber(val number: Int?, val index: Int) : OtpAction
    data class OnChangeFieldFocused(val index: Int) : OtpAction
    data object OnKeyboardBack : OtpAction
}

@Preview
@Composable
private fun Preview() {

    LazyPizzaTheme {
        KoinApplicationPreview(application = { modules(appModule) }) {
            val viewModel: OtpViewModel = koinViewModel()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val focusRequesters = remember {
                (1..4).map { FocusRequester() }
            }
            val focusManager = LocalFocusManager.current
            val keyboardManager = LocalSoftwareKeyboardController.current

            LaunchedEffect(state.focusedIndex) {
                state.focusedIndex?.let {
                    focusRequesters[it].requestFocus()
                }
            }
            LaunchedEffect(state.code, keyboardManager) {
                val allNumbersEntered = state.code.none { it == null }
                if (allNumbersEntered) {
                    focusRequesters.forEach { it.freeFocus() }
                    focusManager.clearFocus()
                    keyboardManager?.hide()
                }
            }
            OtpView(
                state = state,
                onAction = {
                    when (it) {
                        is OtpAction.OnEnterNumber -> {
                            if (it.number != null) {
                                focusRequesters[it.index].freeFocus()
                            }
                        }

                        else -> Unit
                    }
                    viewModel.onAction(it)
                },
                focusRequesters = focusRequesters
            )
        }

    }
}