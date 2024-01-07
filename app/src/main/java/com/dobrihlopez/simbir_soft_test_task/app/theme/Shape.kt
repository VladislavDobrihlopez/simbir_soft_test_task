package com.dobrihlopez.simbir_soft_test_task.app.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class Spacing(
    val extraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 4.dp,
    val spaceMedium: Dp = 8.dp,
    val spaceLarge: Dp = 16.dp,
    val contentPadding: Dp = 12.dp,
)

val LocalSpacing = staticCompositionLocalOf {
    Spacing()
}

val MaterialTheme.spacing: Spacing
    @ReadOnlyComposable
    @Composable
    get() = LocalSpacing.current