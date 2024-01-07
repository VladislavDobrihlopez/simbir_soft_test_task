package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import java.time.LocalDate

@Composable
fun CalendarScreen(
    onNavigateToDetailsScreen: (EventId, Color) -> Unit,
    onCreateNewEvent: (LocalDate) -> Unit
) {
    // viewmodel initialization
    // side effects
    CalendarScreenContent(onDisplayEventDetails = onNavigateToDetailsScreen, onCreateNewEvent = onCreateNewEvent)
}