package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dobrihlopez.simbir_soft_test_task.app.getComponent
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import com.dobrihlopez.simbir_soft_test_task.presentation.model.DetailedEventUiModel
import java.time.LocalDateTime

@Composable
fun EditorScreen(
    eventId: Long?,
    eventCardColor: String?,
    onNavigateBack: () -> Unit,
    viewModel: EditorScreenViewModel = viewModel(
        factory = getComponent().editorComponentBuilder().create(
            eventId,
            eventCardColor
        ).viewModelsFactory()
    ),
) {
    val state = viewModel.state.collectAsState()
    EditorScreenContent(
        state = state.value,
        onNavigateBack = onNavigateBack,
        onSaveEvent = { name: String, description: String, dateStart: LocalDateTime, dateFinish: LocalDateTime ->
            viewModel.onEvent(
                EditorScreenEvent.OnSaveEvent(
                    name,
                    description,
                    dateStart,
                    dateFinish
                )
            )
        }
    )
}