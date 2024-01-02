package com.dobrihlopez.simbir_soft_test_task.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.dobrihlopez.simbir_soft_test_task.domain.model.Day
import com.dobrihlopez.simbir_soft_test_task.domain.model.Month
import com.dobrihlopez.simbir_soft_test_task.domain.model.Year

@Composable
fun AppNavGraph(
    navController: NavHostController,
    calendarScreenContent: @Composable () -> Unit,
    editorScreenContent: @Composable () -> Unit
) {
    NavHost(navController = navController, startDestination = AppScreen.Calendar.route) {
        composable(route = AppScreen.Calendar.route) {
            calendarScreenContent()
        }
        composable(
            route = AppScreen.EventEditor.route,
            arguments = listOf(navArgument(AppScreen.EventEditor.EVENT_ID_PARAM) {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
//            val rawEventId =
//                backStackEntry.arguments?.getString(AppScreen.EventEditor.EVENT_ID_PARAM) ?: ""
//            val eventId = rawEventId.toLong()
            editorScreenContent()
//            with(rawEventId) {
//                val delimiter = '/'
//                val day = substringBefore(delimiter)
//                val month = substringAfter(delimiter).substringBefore(delimiter)
//                val year = substringAfterLast(delimiter)
//                editorScreenContent(Day(day.toInt()), Month(month.toInt()), Year(year.toInt()))
//            }
        }
    }
}