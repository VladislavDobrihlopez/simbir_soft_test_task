package com.dobrihlopez.simbir_soft_test_task.presentation.model

import androidx.compose.ui.graphics.Color
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event

data class DetailedEventUiModel(
    val color: Color,
    val event: Event,
)
