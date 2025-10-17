package dev.mamkin.lazypizza.core.presentation.designsystem.buttons

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            modifier = modifier,
            colors = ButtonDefaults.buttonColors(
                contentColor = AppTheme.colors.textOnPrimary,
                containerColor = Color.Transparent,
                disabledContainerColor = AppTheme.colors.textPrimary8,
                disabledContentColor = AppTheme.colors.textPrimary.copy(alpha = 0.38f)
            ),
            enabled = enabled,
            contentPadding = PaddingValues(),
            shape = CircleShape,
            onClick = onClick
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .then(
                        if (enabled)
                            Modifier.background(
                                brush = brush, // Apply gradient
                                shape = CircleShape // Match button shape
                            ) else Modifier
                    )
                    .padding(horizontal = 24.dp, vertical = 9.dp), // Inner padding
                contentAlignment = Alignment.Center // Center content
            ) {
                Text(
                    text = text,
                    style = AppTheme.typography.title3
                )
            }
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
