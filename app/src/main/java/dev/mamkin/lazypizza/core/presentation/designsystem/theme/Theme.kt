package dev.mamkin.lazypizza.core.presentation.designsystem.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

@Composable
fun LazyPizzaTheme(
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalReplacementTypography provides replacementTypography,
        LocalExtendedColors provides extendedColors
    ) {
        MaterialTheme(
            content = content
        )
    }
}

object AppTheme {
    val typography: ReplacementTypography
        @Composable
        get() = LocalReplacementTypography.current
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}