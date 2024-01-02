package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import androidx.lifecycle.viewModelScope
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartTime
import com.dobrihlopez.simbir_soft_test_task.domain.usecase.GetEventsUseCase
import com.dobrihlopez.simbir_soft_test_task.domain.usecase.UpdateEventDurationUseCase
import com.dobrihlopez.simbir_soft_test_task.presentation.BaseViewModel
import com.dobrihlopez.simbir_soft_test_task.presentation.model.BriefEventUiModel
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val getEventsUseCase: GetEventsUseCase,
    private val updateEventDurationUseCase: UpdateEventDurationUseCase
) : BaseViewModel<
        CalendarScreenState,
        CalendarScreenEvent,
        CalendarSideEffect>(
    initialState = CalendarScreenState.Loading
) {
    init {
        viewModelScope.launch {
            getEventsUseCase().collect {
//                _state.emit(CalendarScreenState.Success())
            }
        }
    }

    override fun onEvent(event: CalendarScreenEvent) {
        when (event) {
            CalendarScreenEvent.OnCreateNewEvent -> onCreateNewEvent()
            is CalendarScreenEvent.OnDisplayEventDetails -> onDisplayEventDetails(event.eventId)
            is CalendarScreenEvent.OnUpdateItemTime -> onUpdateItemTime(
                event.oldEvent,
                event.newStartTime,
                event.newFinishTime
            )
        }
    }

    private fun onCreateNewEvent() {
        executeInCoroutine {
            sideEffects.send(CalendarSideEffect.DisplayCreationEvent)
        }
    }

    private fun onDisplayEventDetails(eventId: EventId) {
        executeInCoroutine {
            sideEffects.send(CalendarSideEffect.DisplayDetailedEvent(eventId))
        }
    }

    private fun onUpdateItemTime(
        oldEvent: BriefEventUiModel,
        newStartTime: StartTime,
        newFinishTime: FinishTime
    ) {
        executeInCoroutine {
            updateEventDurationUseCase(
                eventId = oldEvent.id,
                oldDateTimeStart = oldEvent.dateStart,
                oldDateTimeFinish = oldEvent.dateFinish,
                newStartTime = newStartTime,
                newFinishTime = newFinishTime
            )
        }
    }

    private inline fun executeInCoroutine(crossinline contract: suspend () -> Unit) {
        viewModelScope.launch {
            contract()
        }
    }
}