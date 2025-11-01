package dev.mamkin.lazypizza.core.presentation.designsystem.text_fields

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: @Composable (() -> Unit)? = null,
    colors: TextFieldColors = AppTextFieldDefaults.colors(),
) {
    OutlinedTextField(
        leadingIcon = leadingIcon,
        shape = CircleShape,
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        colors = colors
    )
}

@Immutable
object AppTextFieldDefaults {
    @Composable
    fun colors(): TextFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent,
        errorBorderColor = Color.Transparent,
        focusedContainerColor = AppTheme.colors.surfaceHigher,
        unfocusedContainerColor = AppTheme.colors.surfaceHigher,
        cursorColor = AppTheme.colors.primary
    )
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        AppTextField(
            value = "",
            onValueChange = {}
        )
    }
}
