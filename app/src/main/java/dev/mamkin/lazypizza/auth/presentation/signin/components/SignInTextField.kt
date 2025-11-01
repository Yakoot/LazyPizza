package dev.mamkin.lazypizza.auth.presentation.signin.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import dev.mamkin.lazypizza.core.presentation.designsystem.text_fields.AppTextField
import dev.mamkin.lazypizza.core.presentation.designsystem.text_fields.AppTextFieldDefaults
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun SignInTextField(
    modifier: Modifier = Modifier,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    AppTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = placeholder,
                style = AppTheme.typography.body2Regular,
                color = AppTheme.colors.textSecondary
            )
        },
        colors = AppTextFieldDefaults.colors().copy(
            focusedContainerColor = AppTheme.colors.surfaceHighest,
            unfocusedContainerColor = AppTheme.colors.surfaceHighest
        )
    )
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        SignInTextField(
            value = "",
            onValueChange = {},
            placeholder = "+1 000 000 0000"
        )
    }
}
