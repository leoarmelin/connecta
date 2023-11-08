package com.leoarmelin.connecta.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable


private val LightColors = lightColorScheme(
    primary = mtl_primary,
    onPrimary = mtl_onPrimary,
    primaryContainer = mtl_primaryContainer,
    onPrimaryContainer = mtl_onPrimaryContainer,
    secondary = mtl_secondary,
    onSecondary = mtl_onSecondary,
    secondaryContainer = mtl_secondaryContainer,
    onSecondaryContainer = mtl_onSecondaryContainer,
    tertiary = mtl_tertiary,
    onTertiary = mtl_onTertiary,
    tertiaryContainer = mtl_tertiaryContainer,
    onTertiaryContainer = mtl_onTertiaryContainer,
    error = mtl_error,
    errorContainer = mtl_errorContainer,
    onError = mtl_onError,
    onErrorContainer = mtl_onErrorContainer,
    background = mtl_background,
    onBackground = mtl_onBackground,
    surface = mtl_surface,
    onSurface = mtl_onSurface,
    surfaceVariant = mtl_surfaceVariant,
    onSurfaceVariant = mtl_onSurfaceVariant,
    outline = mtl_outline,
    inverseOnSurface = mtl_inverseOnSurface,
    inverseSurface = mtl_inverseSurface,
    inversePrimary = mtl_inversePrimary,
    surfaceTint = mtl_surfaceTint,
    outlineVariant = mtl_outlineVariant,
    scrim = mtl_scrim,
)


private val DarkColors = darkColorScheme(
    primary = mtd_primary,
    onPrimary = mtd_onPrimary,
    primaryContainer = mtd_primaryContainer,
    onPrimaryContainer = mtd_onPrimaryContainer,
    secondary = mtd_secondary,
    onSecondary = mtd_onSecondary,
    secondaryContainer = mtd_secondaryContainer,
    onSecondaryContainer = mtd_onSecondaryContainer,
    tertiary = mtd_tertiary,
    onTertiary = mtd_onTertiary,
    tertiaryContainer = mtd_tertiaryContainer,
    onTertiaryContainer = mtd_onTertiaryContainer,
    error = mtd_error,
    errorContainer = mtd_errorContainer,
    onError = mtd_onError,
    onErrorContainer = mtd_onErrorContainer,
    background = mtd_background,
    onBackground = mtd_onBackground,
    surface = mtd_surface,
    onSurface = mtd_onSurface,
    surfaceVariant = mtd_surfaceVariant,
    onSurfaceVariant = mtd_onSurfaceVariant,
    outline = mtd_outline,
    inverseOnSurface = mtd_inverseOnSurface,
    inverseSurface = mtd_inverseSurface,
    inversePrimary = mtd_inversePrimary,
    surfaceTint = mtd_surfaceTint,
    outlineVariant = mtd_outlineVariant,
    scrim = mtd_scrim,
)

@Composable
fun ConnectaTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}