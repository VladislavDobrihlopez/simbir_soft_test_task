package com.dobrihlopez.simbir_soft_test_task.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

@Composable
fun Dp.toPx() = with(LocalDensity.current) {
    this@toPx.toPx()
}