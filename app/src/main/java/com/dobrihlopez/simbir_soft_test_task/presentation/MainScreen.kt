package com.dobrihlopez.simbir_soft_test_task.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.dobrihlopez.simbir_soft_test_task.navigation.AppNavGraph
import com.dobrihlopez.simbir_soft_test_task.navigation.Day
import com.dobrihlopez.simbir_soft_test_task.navigation.Month
import com.dobrihlopez.simbir_soft_test_task.navigation.Year
import com.dobrihlopez.simbir_soft_test_task.navigation.rememberAppNavigator
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.CalendarScreen

@Composable
fun MainScreen(navController: NavHostController = rememberNavController()) {
    val navigator by rememberAppNavigator(navController)
    AppNavGraph(
        navController = navController,
        calendarScreenContent = {
//            val counter = rememberSaveable {
//                mutableIntStateOf(0)
//            }
//
//            Column {
//                Text(text = "Calendar")
//                Button(onClick = { counter.value = counter.value + 1 }) {
//                    Text(text = counter.value.toString())
//                }
//                Button(onClick = {
//                    navigator.navigateToEditorScreen(
//                        Day(10),
//                        Month(12),
//                        Year(2023)
//                    )
//                }) {
//                    Text(text = "Navigate to detailed screen")
//                }
//            }
            CalendarScreen()
        },
        editorScreenContent = { day, month, year ->
            Text(text = "$day $month $year")
        }
    )
}