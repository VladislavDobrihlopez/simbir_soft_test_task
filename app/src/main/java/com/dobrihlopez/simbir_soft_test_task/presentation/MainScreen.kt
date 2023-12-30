package com.dobrihlopez.simbir_soft_test_task.presentation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dobrihlopez.simbir_soft_test_task.navigation.AppNavGraph
import com.dobrihlopez.simbir_soft_test_task.navigation.rememberAppNavigator
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.CalendarScreen

@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val navigator by rememberAppNavigator(navController)
    AppNavGraph(
        navController = navController,
        calendarScreenContent = {
            CalendarScreen()
        },
        editorScreenContent = { day, month, year ->
            Text(text = "$day $month $year")
        }
    )
}