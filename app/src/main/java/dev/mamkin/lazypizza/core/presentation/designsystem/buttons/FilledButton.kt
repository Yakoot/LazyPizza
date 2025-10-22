package dev.mamkin.lazypizza.core.presentation.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.AppTheme
import dev.mamkin.lazypizza.core.presentation.designsystem.theme.LazyPizzaTheme

@Composable
fun FilledButton(
    modifier: Modifier = Modifier,
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val brush = Brush.linearGradient(
        listOf(
            AppTheme.colors.primaryGradientStart,
            AppTheme.colors.primaryGradientEnd
        )
    )
    CompositionLocalProvider(LocalRippleConfiguration provides RippleConfiguration(AppTheme.colors.textPrimary)) {
        Button(
            modifier = modifier
                .height(48.dp)
                .clip(CircleShape)
                .then(
                    if (enabled) {
                        Modifier.background(brush)
                    } else {
                        Modifier.background(AppTheme.colors.textPrimary8)
                    }
                ),
            colors = ButtonDefaults.buttonColors(
                contentColor = AppTheme.colors.textOnPrimary,
                containerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = AppTheme.colors.textPrimary.copy(alpha = 0.38f)
            ),
            enabled = enabled,
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 9.dp),
            shape = CircleShape,
            onClick = onClick
        ) {
            Text(
                text = text,
                style = AppTheme.typography.title3
            )
        }
    }


}

@Preview
@Composable
private fun Preview() {
    LazyPizzaTheme {
        Column {
            FilledButton(text = "Button", onClick = {})
            FilledButton(text = "Button", onClick = {}, enabled = false)

        }
    }
}
