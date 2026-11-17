package com.Meztido0.Dewgov.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NeonScheme = darkColorScheme(
    primary = NeonMagenta,
    onPrimary = TextPrimary,
    primaryContainer = MagentaHaze,
    onPrimaryContainer = TextPrimary,
    secondary = ChromeCyan,
    onSecondary = AsphaltBlack,
    secondaryContainer = SurfaceAlt,
    onSecondaryContainer = TextPrimary,
    tertiary = StreetAmber,
    onTertiary = AsphaltBlack,
    background = AsphaltBlack,
    onBackground = TextPrimary,
    surface = SurfaceHud,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    outline = BorderDim,
    outlineVariant = BorderDim,
    error = PatrolRed,
    onError = TextPrimary,
    scrim = SplashTop
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NeonScheme,
        typography = AppTypography,
        content = content
    )
}
