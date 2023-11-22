package com.leoarmelin.connecta.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

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
    val colors = DarkColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colors.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDarkTheme
        }
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}