package dev.mamkin.lazypizza.auth.presentation.signin.components.otp

import android.view.KeyEvent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import dev.mamkin.lazypizza.core.presentation.designsystem.text_fields.AppTextFieldDefaults
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun OtpInputField(
    modifier: Modifier = Modifier,
    number: Int?,
    focusRequester: FocusRequester,
    onFocusChanged: (Boolean) -> Unit,
    onNumberChanged: (Int?) -> Unit,
    onKeyboardBack: () -> Unit,
) {
    val text by remember(number) {
        mutableStateOf(
            TextFieldValue(
                text = number?.toString().orEmpty(),
                selection = TextRange(
                    if (number != null) 1 else 0
                )
            )
        )
    }

    var isFocused by remember {
        mutableStateOf(false)
    }

    val textStyle = AppTheme.typography.body2Regular.copy(
        textAlign = TextAlign.Center
    )

    TextField(
        modifier = modifier
            .height(48.dp)
            .focusRequester(focusRequester)
            .onFocusChanged {
                isFocused = it.isFocused
                onFocusChanged(it.isFocused)
            }
            .onKeyEvent {
                val didPressDelete = it.nativeKeyEvent.keyCode == KeyEvent.KEYCODE_DEL
                if (didPressDelete && number == null) {
                    onKeyboardBack()
                }
                false
            },
        value = text,
        onValueChange = { newText ->
            val newNumber = newText.text
            if (newNumber.length <= 1 && newNumber.isDigitsOnly()) {
                onNumberChanged(newNumber.toIntOrNull())
            }

        },
        shape = CircleShape,
        textStyle = textStyle,
        colors = AppTextFieldDefaults.colors().copy(
            focusedContainerColor = AppTheme.colors.surfaceHigher,
            unfocusedContainerColor = AppTheme.colors.surfaceHighest,
            cursorColor = AppTheme.colors.textSecondary
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        placeholder = {
            if (!isFocused && number == null) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "0",
                    style = textStyle,
                    color = AppTheme.colors.textSecondary
                )
            }
        }
    )
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        OtpInputField(
            modifier = Modifier.width(60.dp),
            number = null,
            focusRequester = remember { FocusRequester() },
            onFocusChanged = {},
            onNumberChanged = { },
            onKeyboardBack = { },
        )
    }
}