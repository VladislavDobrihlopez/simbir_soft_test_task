package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartTime
import com.dobrihlopez.simbir_soft_test_task.domain.usecase.GetEventsUseCase
import com.dobrihlopez.simbir_soft_test_task.domain.usecase.RequestEvents
import com.dobrihlopez.simbir_soft_test_task.domain.usecase.UpdateEventDurationUseCase
import com.dobrihlopez.simbir_soft_test_task.presentation.BaseViewModel
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.WeekSelectorState
import com.dobrihlopez.simbir_soft_test_task.presentation.model.BriefEventUiModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import javax.inject.Inject

class CalendarViewModel @Inject constructor(
    private val getEventsUseCase: GetEventsUseCase,
    private val updateEventDurationUseCase: UpdateEventDurationUseCase,
    private val requestEvents: RequestEvents,
) : BaseViewModel<
        CalendarScreenState,
        CalendarScreenEvent,
        CalendarSideEffect>(
    initialState = CalendarScreenState.Loading(
        WeekSelectorState(
            selectedDate = LocalDate.now(),
            weekData = WeekSelectorState.calculateWeek(newDate = LocalDate.now())
        )
    )
) {
    private val updateItemEvents =
        MutableSharedFlow<Triple<BriefEventUiModel, StartTime, FinishTime>>()

    init {
        updateItemEvents
            .debounce(250)
            .onEach {
                val oldEvent = it.first
                val newStartTime = it.second
                val newFinishTime = it.third
                updateEventDurationUseCase(
                    eventId = it.first.id,
                    oldDateTimeStart = oldEvent.dateStart,
                    oldDateTimeFinish = oldEvent.dateFinish,
                    newStartTime = newStartTime,
                    newFinishTime = newFinishTime
                )
            }
            .launchIn(viewModelScope)
    }

    init {
        getOngoingUpdates()
    }

    private fun getOngoingUpdates() {
        getEventsUseCase { _state.value.weekSelectorState.selectedDate }
            .onEach {
                Log.d("CalendarView", "$it")
                _state.emit(CalendarScreenState.Success(
                    weekSelectorState = state.value.weekSelectorState,
                    events = it.map { it.toBriefEventUiModel() }
                ))
            }
            .launchIn(viewModelScope)
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

            CalendarScreenEvent.OnGoToNextDate -> onChangeDate(true)
            CalendarScreenEvent.OnGoToPreviousDate -> onChangeDate(false)
            is CalendarScreenEvent.OnSelectNewDate -> onSelectNewDate(event.date)
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

    private fun onChangeDate(isMoveForward: Boolean) {
        executeInCoroutine {
            _state.update {
                it as CalendarScreenState.Success
                val weekSelectorState = it.weekSelectorState
                val currentSelectedDate = weekSelectorState.selectedDate
                it.copy(
                    weekSelectorState = weekSelectorState.getUpdatedState(
                        if (isMoveForward)
                            currentSelectedDate.plusDays(1)
                        else
                            currentSelectedDate.minusDays(1)
                    )
                )
            }
            requestEvents()
        }
    }

    private fun onSelectNewDate(date: LocalDate) {
        executeInCoroutine {
            _state.update { oldState ->
                if (oldState is CalendarScreenState.Success) {
                    if (oldState.weekSelectorState.selectedDate == date) return@executeInCoroutine
                    oldState.copy(
                        weekSelectorState = oldState.weekSelectorState.getUpdatedState(
                            date
                        )
                    )
                } else {
                    oldState
                }
            }
            requestEvents()
        }
    }

    private fun onUpdateItemTime(
        oldEvent: BriefEventUiModel,
        newStartTime: StartTime,
        newFinishTime: FinishTime,
    ) {
        executeInCoroutine {
            updateItemEvents.emit(Triple(oldEvent, newStartTime, newFinishTime))
            _state.update { oldState ->
                require(oldState is CalendarScreenState.Success)
                val items = oldState.events.map { eventUiModel ->
                    if (oldEvent.id == eventUiModel.id) {
                        val dateStart =
                            UpdateEventDurationUseCase.calculateTime(
                                oldEvent.dateStart,
                                newStartTime
                            )
                        val dateFinish =
                            UpdateEventDurationUseCase.calculateTime(
                                oldEvent.dateFinish,
                                newFinishTime
                            )
                        oldEvent.copy(
                            dateStart = dateStart,
                            dateFinish = dateFinish,
                        )
                    } else eventUiModel
                }
                oldState.copy(events = items)
            }
        }
    }
}