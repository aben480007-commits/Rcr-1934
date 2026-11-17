package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class ThemeMode {
    LIGHT, DARK, SYSTEM
}

object RcrSpacing {
    val ScreenHorizontal = 20.dp
    val ScreenVertical = 20.dp
    val SectionGap = 28.dp
    val CardGap = 16.dp
    val CardPadding = 20.dp
    val ElementGap = 12.dp
    val ChipGap = 10.dp
    val BottomScreenBuffer = 40.dp
}

@Immutable
data class RcrExtendedColors(
    val isDark: Boolean,
    val primaryBackground: Color,
    val secondaryBackground: Color,
    val cardBackground: Color,
    val deepGreen: Color,
    val primaryGreen: Color,
    val mainText: Color,
    val secondaryText: Color,
    val border: Color,
    val sectionHeading: Color,
    val accentHighlight: Color,
    val chipContainer: Color,
    val rcrRowHighlight: Color
)

private val LightExtendedColors = RcrExtendedColors(
    isDark = false,
    primaryBackground = RcrLightBg,
    secondaryBackground = RcrLightSecondaryBg,
    cardBackground = RcrLightCard,
    deepGreen = RcrDeepGreen,
    primaryGreen = RcrPrimaryGreen,
    mainText = RcrLightTextMain,
    secondaryText = RcrLightTextSecondary,
    border = RcrLightBorder,
    sectionHeading = RcrDeepGreen,
    accentHighlight = RcrPrimaryGreen,
    chipContainer = RcrLightChipBg,
    rcrRowHighlight = RcrPrimaryGreen.copy(alpha = 0.09f)
)

private val DarkExtendedColors = RcrExtendedColors(
    isDark = true,
    primaryBackground = RcrDarkBg,
    secondaryBackground = RcrDarkSecondaryBg,
    cardBackground = RcrDarkCard,
    deepGreen = RcrDeepGreen,
    primaryGreen = RcrPrimaryGreen,
    mainText = RcrDarkTextMain,
    secondaryText = RcrDarkTextSecondary,
    border = RcrDarkBorder,
    sectionHeading = RcrDarkTextMain,
    accentHighlight = RcrDarkGreenHighlight,
    chipContainer = RcrDeepGreen.copy(alpha = 0.35f),
    rcrRowHighlight = RcrPrimaryGreen.copy(alpha = 0.25f)
)

val LocalRcrColors = staticCompositionLocalOf { LightExtendedColors }

private val RcrLightColorScheme = lightColorScheme(
    primary = RcrPrimaryGreen,
    onPrimary = RcrPureWhite,
    primaryContainer = RcrDeepGreen,
    onPrimaryContainer = RcrPureWhite,
    secondary = RcrDeepGreen,
    onSecondary = RcrPureWhite,
    secondaryContainer = RcrLightSecondaryBg,
    onSecondaryContainer = RcrDeepGreen,
    tertiary = RcrGoldAccent,
    onTertiary = RcrLightTextMain,
    background = RcrLightBg,
    onBackground = RcrLightTextMain,
    surface = RcrLightBg,
    onSurface = RcrLightTextMain,
    surfaceVariant = RcrLightSecondaryBg,
    onSurfaceVariant = RcrLightTextSecondary,
    surfaceContainerLowest = RcrLightCard,
    surfaceContainerLow = RcrLightSecondaryBg,
    surfaceContainer = RcrLightCard,
    surfaceContainerHigh = RcrLightSecondaryBg,
    surfaceContainerHighest = RcrLightBorder,
    outline = RcrLightBorder,
    outlineVariant = RcrLightBorder,
    error = RcrLiveRed,
    onError = RcrPureWhite
)

private val RcrDarkColorScheme = darkColorScheme(
    primary = RcrPrimaryGreen,
    onPrimary = RcrPureWhite,
    primaryContainer = RcrDeepGreen,
    onPrimaryContainer = RcrPureWhite,
    secondary = RcrDarkGreenHighlight,
    onSecondary = RcrDarkBg,
    secondaryContainer = RcrDarkSecondaryBg,
    onSecondaryContainer = RcrDarkTextMain,
    tertiary = RcrGoldAccent,
    onTertiary = RcrDarkBg,
    background = RcrDarkBg,
    onBackground = RcrDarkTextMain,
    surface = RcrDarkSecondaryBg,
    onSurface = RcrDarkTextMain,
    surfaceVariant = RcrDarkCard,
    onSurfaceVariant = RcrDarkTextSecondary,
    surfaceContainerLowest = RcrDarkBg,
    surfaceContainerLow = RcrDarkSecondaryBg,
    surfaceContainer = RcrDarkCard,
    surfaceContainerHigh = RcrDarkCardElevated,
    surfaceContainerHighest = RcrDarkBorder,
    outline = RcrDarkBorder,
    outlineVariant = RcrDarkBorder,
    error = RcrLiveRed,
    onError = RcrPureWhite
)

val RcrShapes = Shapes(
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(18.dp),
    large = RoundedCornerShape(22.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun RcrTheme(
    themeMode: ThemeMode = ThemeMode.LIGHT,
    content: @Composable () -> Unit
) {
    val useDark = when (themeMode) {
        ThemeMode.LIGHT -> false
        ThemeMode.DARK -> true
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val extendedColors = if (useDark) DarkExtendedColors else LightExtendedColors

    CompositionLocalProvider(LocalRcrColors provides extendedColors) {
        MaterialTheme(
            colorScheme = if (useDark) RcrDarkColorScheme else RcrLightColorScheme,
            typography = RcrTypography,
            shapes = RcrShapes,
            content = content
        )
    }
}
