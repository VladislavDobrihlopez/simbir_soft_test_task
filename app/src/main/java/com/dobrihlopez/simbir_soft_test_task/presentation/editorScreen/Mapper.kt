package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen

import androidx.compose.ui.graphics.Color
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import com.dobrihlopez.simbir_soft_test_task.presentation.model.BriefEventUiModel
import com.dobrihlopez.simbir_soft_test_task.presentation.model.DetailedEventUiModel

fun Event.toDetailedEventUiModel(color: Color): DetailedEventUiModel {
    return DetailedEventUiModel(
        color = color,
        event = this
    )
}