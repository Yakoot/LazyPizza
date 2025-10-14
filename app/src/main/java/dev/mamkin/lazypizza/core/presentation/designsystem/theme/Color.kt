package dev.mamkin.lazypizza.core.presentation.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

val TextPrimary = Color(0xFF03131F)
val TextPrimary8 = Color(0x1403131F)
val TextSecondary = Color(0xFF627686)
val TextSecondary8 = Color(0x14627686) // 8% of #627686
val TextOnPrimary = Color(0xFFFFFFFF)
val BG = Color(0xFFFAFBFC)
val SurfaceHigher = Color(0xFFFFFFFF)
val SurfaceHighest = Color(0xFFF0F3F6)
val Outline = Color(0xFFE6E7ED)
val Outline50 = Color(0x80E6E7ED) // 50% of #E6E7ED
val PrimaryGradientStart = Color(0xFFF9966F)
val PrimaryGradientEnd = Color(0xFFF36B50)
val Primary = Color(0xFFF36B50)
val Primary8 = Color(0x14F36B50) // 8% of #F36B50

data class ExtendedColors(
    val textPrimary: Color,
    val textPrimary8: Color,
    val textSecondary: Color,
    val textSecondary8: Color,
    val textOnPrimary: Color,
    val bg: Color,
    val surfaceHigher: Color,
    val surfaceHighest: Color,
    val outline: Color,
    val outline50: Color,
    val primaryGradientStart: Color,
    val primaryGradientEnd: Color,
    val primary: Color,
    val primary8: Color,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(
        textPrimary = Color.Unspecified,
        textPrimary8 = Color.Unspecified,
        textSecondary = Color.Unspecified,
        textSecondary8 = Color.Unspecified,
        textOnPrimary = Color.Unspecified,
        bg = Color.Unspecified,
        surfaceHigher = Color.Unspecified,
        surfaceHighest = Color.Unspecified,
        outline = Color.Unspecified,
        outline50 = Color.Unspecified,
        primaryGradientStart = Color.Unspecified,
        primaryGradientEnd = Color.Unspecified,
        primary = Color.Unspecified,
        primary8 = Color.Unspecified
    )
}

val extendedColors = ExtendedColors(
    textPrimary = TextPrimary,
    textPrimary8 = TextPrimary8,
    textSecondary = TextSecondary,
    textSecondary8 = TextSecondary8,
    textOnPrimary = TextOnPrimary,
    bg = BG,
    surfaceHigher = SurfaceHigher,
    surfaceHighest = SurfaceHighest,
    outline = Outline,
    outline50 = Outline50,
    primaryGradientStart = PrimaryGradientStart,
    primaryGradientEnd = PrimaryGradientEnd,
    primary = Primary,
    primary8 = Primary8
)
