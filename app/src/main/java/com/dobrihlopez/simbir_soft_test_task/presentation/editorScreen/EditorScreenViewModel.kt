package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import com.dobrihlopez.simbir_soft_test_task.R
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import com.dobrihlopez.simbir_soft_test_task.domain.usecase.CreateEventUseCase
import com.dobrihlopez.simbir_soft_test_task.domain.usecase.GetDetailedEventUseCase
import com.dobrihlopez.simbir_soft_test_task.domain.usecase.UpdateEventUseCase
import com.dobrihlopez.simbir_soft_test_task.navigation.AppScreen
import com.dobrihlopez.simbir_soft_test_task.presentation.BaseViewModel
import com.dobrihlopez.simbir_soft_test_task.presentation.UiText
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

class EditorScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getDetailedEventUseCase: GetDetailedEventUseCase,
    private val createEventUseCase: CreateEventUseCase,
    private val updateEventUseCase: UpdateEventUseCase,
) : BaseViewModel<EditorScreenState, EditorScreenEvent, EditorSideEffect>(EditorScreenState.Loading) {
    private val id = savedStateHandle.get<Long>(AppScreen.EventEditor.EVENT_ID_PARAM)
    private val color = savedStateHandle.get<ULong>(AppScreen.EventEditor.EVENT_ITEM_COLOR)

    private val mode: ScreenMode
        get() = if (color != null && id != null) {
            ScreenMode.EDITOR
        } else {
            ScreenMode.CREATOR
        }

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        _state.update {
            EditorScreenState.Error(UiText.Runtime(throwable.message.toString()))
        }
    }

    init {
        if (mode == ScreenMode.EDITOR) {
            setupEditorMode()
        } else {
            setupCreatorMode()
        }
    }

    override fun onEvent(event: EditorScreenEvent) {
        when (event) {
            EditorScreenEvent.OnNavigateBack -> onNavigateBack()
            is EditorScreenEvent.OnSaveEvent -> onSaveEvent(
                name = event.name,
                description = event.description,
                dateStart = event.dateStart,
                dateFinish = event.dateFinish
            )

            is EditorScreenEvent.OnUpdateFinishEventTime -> TODO()
            is EditorScreenEvent.OnUpdateStartEventTime -> TODO()
        }
    }

    private fun setupEditorMode() {
        executeInCoroutine(exceptionHandler) {
            val id = requireNotNull(id)
            val colorValue = requireNotNull(color)
            getDetailedEventUseCase(id)
                .onSuccess { event ->
                    _state.update {
                        EditorScreenState.Success.EditMode(
                            event.toDetailedEventUiModel(
                                Color(
                                    colorValue
                                )
                            )
                        )
                    }
                }
                .onFailure { ex ->
                    _state.update {
                        EditorScreenState.Error(UiText.Resource(R.string.string_failure_when_fetching_event_details))
                    }
                }
        }
    }

    private fun setupCreatorMode() {
        executeInCoroutine(exceptionHandler) {
            _state.update {
                val currentTime = LocalDateTime.now()
                EditorScreenState.Success.CreatorMode(
                    Event(
                        id = -1, name = "", description = "",
                        dateStart = currentTime,
                        dateFinish = currentTime
                    )
                )
            }
        }
    }

    private fun onSaveEvent(
        name: String,
        description: String,
        dateStart: LocalDateTime,
        dateFinish: LocalDateTime
    ) {
        executeInCoroutine(exceptionHandler) {
            when (mode) {
                ScreenMode.EDITOR -> {
                    updateEventUseCase(
                        requireNotNull(id),
                        name,
                        description,
                        dateStart,
                        dateFinish
                    )
                }

                ScreenMode.CREATOR -> {
                    createEventUseCase(
                        name,
                        description,
                        dateStart,
                        dateFinish
                    )
                }
            }
        }
    }

    private fun onNavigateBack() {
        executeInCoroutine {
            sideEffects.send(EditorSideEffect.OnNavigateBack)
        }
    }
}