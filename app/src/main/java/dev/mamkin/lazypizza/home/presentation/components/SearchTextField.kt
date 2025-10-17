package dev.mamkin.lazypizza.home.presentation.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.mamkin.lazypizza.R
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.search_refraction),
                contentDescription = null,
                tint = AppTheme.colors.primary
            )
        },
        shape = CircleShape,
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(
                text = stringResource(R.string.search_placeholder),
                style = AppTheme.typography.body1Regular,
                color = AppTheme.colors.textSecondary
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent,
            errorBorderColor = Color.Transparent,
            focusedContainerColor = AppTheme.colors.surfaceHigher,
            unfocusedContainerColor = AppTheme.colors.surfaceHigher,
            cursorColor = AppTheme.colors.primary
        )
    )
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        SearchTextField(
            value = "",
            onValueChange = {}
        )
    }
}