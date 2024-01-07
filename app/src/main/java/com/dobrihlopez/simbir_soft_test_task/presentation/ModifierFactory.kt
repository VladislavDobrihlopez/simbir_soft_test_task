package com.dobrihlopez.simbir_soft_test_task.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import com.dobrihlopez.simbir_soft_test_task.app.theme.LocalSpacing

@Composable
fun Modifier.Wrapper(
    shape: Shape = MaterialTheme.shapes.small,
    background: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
): Modifier {
    val spacing = LocalSpacing.current
    return this
        .clip(shape)
        .background(background)
        .padding(spacing.spaceSmall)
}