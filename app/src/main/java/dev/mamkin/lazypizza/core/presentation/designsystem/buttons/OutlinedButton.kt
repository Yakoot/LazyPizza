package dev.mamkin.lazypizza.core.presentation.designsystem.buttons

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun OutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = AppTheme.colors.primary,
            disabledContentColor = AppTheme.colors.textPrimary.copy(alpha = 0.38f)
        ),
        enabled = enabled,
        contentPadding = PaddingValues(
            horizontal = 24.dp,
            vertical = 9.dp
        ),
        border = BorderStroke(
            width = 1.dp,
            color = AppTheme.colors.primary8
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
            OutlinedButton(
                text = "Button"
            ) { }
            OutlinedButton(
                text = "Button",
                enabled = false
            ) { }
        }
    }
}
