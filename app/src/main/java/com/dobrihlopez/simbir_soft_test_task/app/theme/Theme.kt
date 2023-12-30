package com.dobrihlopez.simbir_soft_test_task.app.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(26, 115, 232),
    onPrimary = Color.White,
    primaryContainer = Color(0XFF414659),
    onPrimaryContainer = Black,

    secondary = Color(0xFF1e202c),
    onSecondary = Color(25, 103, 210),

    surface = Color(0XFF10131a),
    onSurface = Color.White,

//    background = Color.White,
//    onBackground = Color(32, 33, 36),
    error = Color(217, 48, 37),
    onError = Color.White,
)

private val LightColorScheme = lightColorScheme(
    primary = Color(26, 115, 232),
    onPrimary = Color.White,
    primaryContainer = Color(0XFFb4e5f2),
    onPrimaryContainer = Black,
    secondary = Color(232, 240, 254),
    onSecondary = Color(25, 103, 210),
    surface = Color.White,
    onSurface = Color(60, 64, 67),
//    background = Color.White,
//    onBackground = Color(32, 33, 36),
    error = Color(217, 48, 37),
    onError = Color.White,
)

@Composable
fun Simbir_soft_test_taskTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}