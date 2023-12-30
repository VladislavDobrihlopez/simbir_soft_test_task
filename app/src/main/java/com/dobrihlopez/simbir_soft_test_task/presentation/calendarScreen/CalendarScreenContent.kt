package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.ScheduleChart
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.ScheduleChartState
import com.dobrihlopez.simbir_soft_test_task.presentation.model.EventUiModel
import java.time.LocalDateTime

@Composable
fun CalendarScreenContent() {
    val chartState = remember {
        mutableStateOf(
            ScheduleChartState(
                listOf(
                    EventUiModel(
                        id = 0,
                        name = "Wilbert Chapman",
                        color = Color.White,
                        dateStart = LocalDateTime.now(),
                        dateFinish = LocalDateTime.now().plusHours(1)
                    ),
                    EventUiModel(
                        id = 1,
                        name = "Wilbert Chapman",
                        color = Color.Green,
                        dateStart = LocalDateTime.now().minusHours(3),
                        dateFinish = LocalDateTime.now().minusMinutes(30)
                    )
                )
            )
        )
    }
    ScheduleChart(
        modifier = Modifier.fillMaxSize(),
        state = chartState,
        onComposableSizeDefined = { size ->
            chartState.value = chartState.value.copy(componentSize = size)
        },
        onTransformationChange = { visibleItems, scrollPos ->
            chartState.value =
                chartState.value.copy(
                    scrolledYaxis = scrollPos.value,
                    visibleHourBlocks = visibleItems.value
                )
        })
}