package com.dobrihlopez.simbir_soft_test_task.presentation.model

import androidx.compose.ui.graphics.Color
import java.time.LocalDateTime

data class EventUiModel(
    val id: Long,
    val name: String,
    val color: Color,
    val onColor: Color,
    val dateStart: LocalDateTime,
    val dateFinish: LocalDateTime,
)