package com.dobrihlopez.simbir_soft_test_task.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import java.time.format.DateTimeFormatter

@Composable
fun Dp.toPx() = with(LocalDensity.current) {
    this@toPx.toPx()
}

val formatterDate = DateTimeFormatter.ofPattern("dd.MM.yyyy")
val formatterTime = DateTimeFormatter.ofPattern("HH:mm")
