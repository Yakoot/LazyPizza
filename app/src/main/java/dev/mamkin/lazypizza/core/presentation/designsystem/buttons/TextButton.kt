package dev.mamkin.lazypizza.core.presentation.designsystem.buttons

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    TextButton(
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            contentColor = AppTheme.colors.primary,
            disabledContentColor = AppTheme.colors.textPrimary.copy(alpha = 0.38f)
        ),
        enabled = enabled,
        contentPadding = PaddingValues(
            horizontal = 12.dp,
            vertical = 9.dp
        ),
        shape = CircleShape,
        onClick = onClick
    ) {
        Text(
            text = text,
            style = AppTheme.typography.title3
        )
    }
}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        Column {
            TextButton(text = "Button", onClick = {})
            TextButton(text = "Button", onClick = {}, enabled = false)

        }
    }
}
