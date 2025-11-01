package dev.mamkin.lazypizza.core.presentation.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import dev.mamkin.lazypizza.R

val InstrumentSans = FontFamily(
    Font(R.font.instrument_sans, FontWeight.Normal),
    Font(R.font.instrument_sans_medium, FontWeight.Medium),
    Font(R.font.instrument_sans_semibold, FontWeight.SemiBold),
    Font(R.font.instrument_sans_bold, FontWeight.Bold),
)

@Immutable
data class ReplacementTypography(
    val title1Medium: TextStyle,
    val title1SemiBold: TextStyle,
    val title2: TextStyle,
    val title3: TextStyle,
    val title4: TextStyle,
    val label2SemiBold: TextStyle,
    val body1Regular: TextStyle,
    val body1Medium: TextStyle,
    val body3Regular: TextStyle,
    val body3Medium: TextStyle,
    val body3Bold: TextStyle,
    val body4Regular: TextStyle,
)

val LocalReplacementTypography = staticCompositionLocalOf {
    ReplacementTypography(
        title1Medium = TextStyle.Default,
        title1SemiBold = TextStyle.Default,
        title2 = TextStyle.Default,
        title3 = TextStyle.Default,
        title4 = TextStyle.Default,
        label2SemiBold = TextStyle.Default,
        body1Regular = TextStyle.Default,
        body1Medium = TextStyle.Default,
        body3Regular = TextStyle.Default,
        body3Medium = TextStyle.Default,
        body3Bold = TextStyle.Default,
        body4Regular = TextStyle.Default
    )
}

val replacementTypography = ReplacementTypography(
    title1Medium = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Medium,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    ),
    title1SemiBold = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
    ),
    title2 = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 24.sp,
    ),
    title3 = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.sp,
    ),
    title4 = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
    ),
    label2SemiBold = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
    body1Regular = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    body1Medium = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 22.sp,
    ),
    body3Regular = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    ),
    body3Medium = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    ),
    body3Bold = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 18.sp,
    ),
    body4Regular = TextStyle(
        fontFamily = InstrumentSans,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    )
)