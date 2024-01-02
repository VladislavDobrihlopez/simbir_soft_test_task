package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartTime
import com.dobrihlopez.simbir_soft_test_task.presentation.UiText
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.WeekSelectorState
import com.dobrihlopez.simbir_soft_test_task.presentation.model.BriefEventUiModel

sealed class CalendarScreenState {
    data object Loading : CalendarScreenState()
    data class Error(val text: UiText) : CalendarScreenState()
    data class Success(
        val weekSelectorState: WeekSelectorState,
        val events: List<BriefEventUiModel>
    ) : CalendarScreenState()
}

sealed class CalendarScreenEvent {
    data class OnDisplayEventDetails(val eventId: EventId) : CalendarScreenEvent()
    data object OnCreateNewEvent : CalendarScreenEvent()
    data class OnUpdateItemTime(
        val oldEvent: BriefEventUiModel,
        val newStartTime: StartTime,
        val newFinishTime: FinishTime
    ) : CalendarScreenEvent()
}

sealed class CalendarSideEffect {
    data class DisplayDetailedEvent(val eventId: EventId) : CalendarSideEffect()
    data object DisplayCreationEvent : CalendarSideEffect()
}