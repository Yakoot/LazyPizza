package dev.mamkin.lazypizza.core.presentation.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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