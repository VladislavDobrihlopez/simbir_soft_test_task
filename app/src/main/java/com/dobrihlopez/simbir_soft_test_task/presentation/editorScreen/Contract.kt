package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen

import com.dobrihlopez.simbir_soft_test_task.presentation.UiText
import com.dobrihlopez.simbir_soft_test_task.presentation.model.DetailedEventUiModel
import java.time.LocalDateTime

sealed class EditorScreenState {
    data object Loading : EditorScreenState()
    data class Error(val text: UiText) : EditorScreenState()
    data class Success(
        val extendedEvent: DetailedEventUiModel,
    ) : EditorScreenState()
}

sealed class EditorScreenEvent {
    data class OnSaveEvent(
        val name: String,
        val description: String,
        val dateStart: LocalDateTime,
        val dateFinish: LocalDateTime
    ) : EditorScreenEvent()

    data object OnNavigateBack : EditorScreenEvent()
}

sealed class EditorSideEffect {
    data object OnNavigateBack : EditorSideEffect()
}