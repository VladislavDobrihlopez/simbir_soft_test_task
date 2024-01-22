package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartTime
import com.dobrihlopez.simbir_soft_test_task.presentation.UiText
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.WeekSelectorState
import com.dobrihlopez.simbir_soft_test_task.presentation.model.BriefEventUiModel
import java.time.LocalDate

sealed class CalendarScreenState(open val weekSelectorState: WeekSelectorState) {
    data class Loading(override val weekSelectorState: WeekSelectorState) : CalendarScreenState(weekSelectorState)
    data class Error(val text: UiText, override val weekSelectorState: WeekSelectorState) :
        CalendarScreenState(weekSelectorState)

    data class Success(
        override val weekSelectorState: WeekSelectorState,
        val events: List<BriefEventUiModel>,
    ) : CalendarScreenState(weekSelectorState)
}

sealed class CalendarScreenEvent {
    data class OnDisplayEventDetails(val eventId: EventId) : CalendarScreenEvent()
    data object OnCreateNewEvent : CalendarScreenEvent()
    data object OnGoToNextDate : CalendarScreenEvent()
    data object OnGoToPreviousDate : CalendarScreenEvent()
    data class OnUpdateItemTime(
        val oldEvent: BriefEventUiModel,
        val newStartTime: StartTime,
        val newFinishTime: FinishTime,
    ) : CalendarScreenEvent()
    data class OnSelectNewDate(val date: LocalDate): CalendarScreenEvent()
}

sealed class CalendarSideEffect {
    data class DisplayDetailedEvent(val eventId: EventId) : CalendarSideEffect()
    data object DisplayCreationEvent : CalendarSideEffect()
}