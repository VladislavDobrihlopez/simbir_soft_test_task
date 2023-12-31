package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.dobrihlopez.simbir_soft_test_task.app.theme.LocalSpacing
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.FinishTime
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.StartTime
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.ScheduleChart
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.WeekSelector
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.rememberScheduleChartState
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.rememberWeekSelectorState
import com.dobrihlopez.simbir_soft_test_task.presentation.model.EventUiModel
import java.time.LocalDate
import java.time.LocalDateTime

@Composable
fun CalendarScreenContent() {
    val items = remember {
        mutableStateOf<List<EventUiModel>>(buildList {
            add(
                EventUiModel(
                    id = 0,
                    name = "Houston",
                    dateStart = LocalDateTime.now().minusHours(5),
                    dateFinish = LocalDateTime.now().minusHours(2)
                )
            )
            add(
                EventUiModel(
                    id = 1,
                    name = "Hello",
                    dateStart = LocalDateTime.now().minusHours(4),
                    dateFinish = LocalDateTime.now().minusHours(3)
                )
            )
            add(
                EventUiModel(
                    id = 4,
                    name = "Hello",
                    dateStart = LocalDateTime.now().minusHours(4).plusMinutes(25),
                    dateFinish = LocalDateTime.now().minusHours(3).minusMinutes(20)
                )
            )
            add(
                EventUiModel(
                    id = 2,
                    name = "Wilbert Chapman",
                    dateStart = LocalDateTime.now().minusHours(2),
                    dateFinish = LocalDateTime.now().plusMinutes(30)
                )
            )
        })
    }
    val chartState = rememberScheduleChartState(
        events = items.value
    )

    val selectedDate = LocalDate.now()

    val dates = buildList<LocalDate> {
        add(selectedDate)
        repeat(4) { index ->
            add(LocalDate.now().plusDays((index + 1).toLong()))
        }
    }

    val selectorState = rememberWeekSelectorState(weekData = dates, selectedDate = selectedDate)

    val context = LocalContext.current
    val spacing = LocalSpacing.current
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(spacing.spaceMedium)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            WeekSelector(
                modifier = Modifier.fillMaxWidth(),
                state = selectorState,
                onMoveToPreviousDate = { /*TODO*/ },
                onMoveToNextDate = { /*TODO*/ },
                onSelectNewDay = {

                }
            )
            ScheduleChart(
                modifier = Modifier.fillMaxSize(),
                state = chartState,
                onComposableSizeDefined = { size ->
                    chartState.value = chartState.value.copy(componentSize = size)
                },
                onTransformationChange = { visibleItems, scrollPos ->
                    chartState.value =
                        chartState.value.copy(
                            scrolledYaxis = scrollPos,
                            visibleHourBlocks = visibleItems
                        )
                },
                onTapItem = { event ->
                    Toast.makeText(context, "$event", Toast.LENGTH_SHORT).show()
                },
                onUpdateItem = { eventUiModel: EventUiModel, localTime: StartTime, localTime2: FinishTime ->
                    items.value = items.value.map {
                        if (it.id == eventUiModel.id) it.copy(
                            dateStart = it.dateStart
                                .withHour(localTime.hour)
                                .withMinute(localTime.minute),
                            dateFinish = it.dateFinish
                                .withHour(localTime2.hour)
                                .withMinute(localTime2.minute),
                        ) else it
                    }
                    chartState.value = chartState.value.copy(events = items.value)
                })
        }
    }

}