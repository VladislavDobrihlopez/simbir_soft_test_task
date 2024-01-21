package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.dobrihlopez.simbir_soft_test_task.app.getComponent
import java.time.LocalDate

@Composable
fun CalendarScreen(
    onNavigateToDetailsScreen: (EventId, Color) -> Unit,
    onCreateNewEvent: (LocalDate) -> Unit,
    viewModel: CalendarViewModel = viewModel(factory = getComponent().getViewModelFactory()),
) {
    // viewmodel initialization
    // side effects
    val state by viewModel.state.collectAsState()
    CalendarScreenContent(
        state = state,
        onDisplayEventDetails = onNavigateToDetailsScreen,
        onCreateNewEvent = onCreateNewEvent
    )
}