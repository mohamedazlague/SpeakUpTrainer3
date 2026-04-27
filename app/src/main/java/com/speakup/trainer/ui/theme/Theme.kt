package com.speakup.trainer.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// ─── Dark Color Scheme ────────────────────────────────────────────────────────
private val DarkColorScheme = darkColorScheme(
    primary          = PrimaryBlue,
    onPrimary        = DarkBackground,
    primaryContainer = PrimaryBlueDark,
    secondary        = SecondaryTeal,
    onSecondary      = DarkBackground,
    tertiary         = PurpleAccent,
    background       = DarkBackground,
    onBackground     = DarkOnBackground,
    surface          = DarkSurface,
    onSurface        = DarkOnSurface,
    surfaceVariant   = DarkCard,
    outline          = DarkCard
)

// ─── Light Color Scheme ───────────────────────────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary          = PrimaryBlue,
    onPrimary        = LightBackground,
    primaryContainer = LightCard,
    secondary        = SecondaryTeal,
    onSecondary      = LightBackground,
    tertiary         = PurpleAccent,
    background       = LightBackground,
    onBackground     = LightOnBackground,
    surface          = LightSurface,
    onSurface        = LightOnSurface,
    surfaceVariant   = LightCard,
    outline          = LightCard
)

/**
 * Core theme composable. Wraps the entire app.
 * Automatically switches between dark and light based on system settings.
 */
@Composable
fun SpeakUpTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography  = AppTypography,
        content     = content
    )
}
