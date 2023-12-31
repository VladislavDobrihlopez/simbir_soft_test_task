package com.dobrihlopez.simbir_soft_test_task.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dobrihlopez.simbir_soft_test_task.domain.model.Day
import com.dobrihlopez.simbir_soft_test_task.domain.model.Month
import com.dobrihlopez.simbir_soft_test_task.domain.model.Year
import com.dobrihlopez.simbir_soft_test_task.navigation.AppNavGraph
import com.dobrihlopez.simbir_soft_test_task.navigation.rememberAppNavigator
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.CalendarScreen
import com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen.EditorScreen

@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val navigator by rememberAppNavigator(navController)
    AppNavGraph(
        navController = navController,
        calendarScreenContent = {
            CalendarScreen(onNavigateToDetailsScreen = {
                navigator.navigateToEditorScreen(Day(12), Month(12), Year(2012))
            })
        },
        editorScreenContent = { day, month, year ->
            EditorScreen()
        }
    )
}