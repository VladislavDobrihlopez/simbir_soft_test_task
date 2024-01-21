package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import com.dobrihlopez.simbir_soft_test_task.presentation.model.DetailedEventUiModel
import java.time.LocalDateTime

@Composable
fun EditorScreen(
    eventId: Long?,
    eventCardColor: String?,
    onNavigateBack: () -> Unit,
) {
    EditorScreenContent(
        state = EditorScreenState.Success.EditMode(
            DetailedEventUiModel(
                color = if (eventCardColor != null) Color(eventCardColor.toULong()) else Color.Transparent,
                event = Event(
                    id = eventId ?: -1,
                    name = "qwerty",
                    description = "А зачем убирать явное создание вьюхолдера в onCreateViewHolder? Ресайклер же перед вызовом onCreateViewHolder лезет в пул. И если не находит нужный тип - вызывает уже onCreateViewHolder. А проверку на присутствие вьюхолдера внутри пула я не могу, да и незачем: ресайклер сам под капотом делает эту проверку.\n" +
                            "Непонятно почему он не берет те вьюхолдеры, которые создаю заранее. С вьютайпом не ошибся, по логам вижу",
                    dateStart = LocalDateTime.now().minusHours(3),
                    dateFinish = LocalDateTime.now().minusHours(1)
                )
            )
        ),
        onNavigateBack = onNavigateBack,
        onSaveEvent = { s: String, s1: String, localDateTime: LocalDateTime, localDateTime1: LocalDateTime ->
            // work with viewmodel
        }
    )
}