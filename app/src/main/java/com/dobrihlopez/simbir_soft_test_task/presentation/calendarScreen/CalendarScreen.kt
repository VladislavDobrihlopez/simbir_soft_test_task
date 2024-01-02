package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import androidx.compose.runtime.Composable



@Composable
fun CalendarScreen(onNavigateToDetailsScreen: (EventId) -> Unit) {
    // viewmodel initialization
    // side effects
    CalendarScreenContent(onDisplayEventDetails = onNavigateToDetailsScreen, onCreateNewEvent = {})
}