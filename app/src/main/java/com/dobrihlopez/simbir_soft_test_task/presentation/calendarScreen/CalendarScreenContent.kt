package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.dobrihlopez.simbir_soft_test_task.app.theme.LocalSpacing
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartTime
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.FabEventCreator
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.ScheduleChart
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.WeekSelector
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.rememberScheduleChartState
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable.rememberWeekSelectorState
import com.dobrihlopez.simbir_soft_test_task.presentation.model.BriefEventUiModel
import java.time.LocalDate
import java.time.LocalDateTime

typealias EventId = Long

@Composable
fun CalendarScreenContent(
    state: CalendarScreenState,
    onDisplayEventDetails: (EventId, Color) -> Unit,
    onCreateNewEvent: (LocalDate) -> Unit,
    onUpdateEvent: (BriefEventUiModel, StartTime, FinishTime) -> Unit,
    onGoToNextDate: () -> Unit,
    onGoToPreviousDate: () -> Unit,
    onSelectDate: (LocalDate) -> Unit
) {
    var selectedDate by remember(state.weekSelectorState) {
        mutableStateOf(state.weekSelectorState.selectedDate)
    }

    val selectorState =
        rememberWeekSelectorState(
            weekData = state.weekSelectorState.weekData,
            selectedDate = state.weekSelectorState.selectedDate
        )

    LaunchedEffect(key1 = state.weekSelectorState) {
        selectorState.value = state.weekSelectorState
    }

    val context = LocalContext.current
    val spacing = LocalSpacing.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FabEventCreator(onTouch = {
                onCreateNewEvent(selectedDate)
            })
        }
    ) { paddingValues: PaddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(spacing.contentPadding)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                WeekSelector(
                    modifier = Modifier.fillMaxWidth(),
                    state = selectorState,
                    onMoveToPreviousDate = onGoToPreviousDate,
                    onMoveToNextDate = onGoToNextDate,
                    onSelectNewDay = onSelectDate
                )

                when (state) {
                    is CalendarScreenState.Error -> {

                    }

                    is CalendarScreenState.Loading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    }

                    is CalendarScreenState.Success -> {

                        // react to screen states

//    val items = remember {
//        mutableStateOf<List<BriefEventUiModel>>(buildList {
//            add(
//                BriefEventUiModel(
//                    id = 0,
//                    name = "Houston",
//                    dateStart = LocalDateTime.now().minusHours(5),
//                    dateFinish = LocalDateTime.now().minusHours(2)
//                )
//            )
//            add(
//                BriefEventUiModel(
//                    id = 1,
//                    name = "Hello",
//                    dateStart = LocalDateTime.now().minusHours(4),
//                    dateFinish = LocalDateTime.now().minusHours(3)
//                )
//            )
//            add(
//                BriefEventUiModel(
//                    id = 4,
//                    name = "Hello",
//                    dateStart = LocalDateTime.now().minusHours(4).plusMinutes(25),
//                    dateFinish = LocalDateTime.now().minusHours(3).minusMinutes(20)
//                )
//            )
//            add(
//                BriefEventUiModel(
//                    id = 2,
//                    name = "Wilbert Chapman",
//                    dateStart = LocalDateTime.now().minusHours(2),
//                    dateFinish = LocalDateTime.now().plusMinutes(30)
//                )
//            )
//        })
//    }
                        val chartState = rememberScheduleChartState(
                            events = state.events
                        )

                        LaunchedEffect(key1 = state.events) {
                            chartState.value = chartState.value.copy(events = state.events)
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
                                        scrolledYaxis = scrollPos,
                                        visibleHourBlocks = visibleItems
                                    )
                            },
                            onTouchItem = { event, color ->
                                Toast.makeText(context, "$event", Toast.LENGTH_SHORT).show()
                                onDisplayEventDetails(event.id, color)
                            },
                            onUpdateItem = onUpdateEvent)
                    }
                }
            }
        }
    }
}