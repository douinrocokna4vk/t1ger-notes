package com.Telqopu0.Kitdag.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val FortunaColorScheme = lightColorScheme(
    primary = TigerOrange,
    onPrimary = OnPrimary,
    primaryContainer = AmberOrange,
    onPrimaryContainer = OnPrimary,
    secondary = AmberOrange,
    onSecondary = OnPrimary,
    secondaryContainer = SurfaceAlt,
    onSecondaryContainer = TextPrimary,
    tertiary = LuckyGold,
    onTertiary = TextPrimary,
    tertiaryContainer = PaleGold,
    onTertiaryContainer = TextPrimary,
    background = BgCream,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = BorderWarm,
    outlineVariant = SurfaceMuted,
    error = Danger,
    onError = OnPrimary,
    scrim = LacquerDark
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FortunaColorScheme,
        typography = FortunaTypography,
        content = content
    )
}
