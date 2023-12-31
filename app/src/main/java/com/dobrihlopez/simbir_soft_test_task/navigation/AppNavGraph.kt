package com.dobrihlopez.simbir_soft_test_task.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun AppNavGraph(
    navController: NavHostController,
    calendarScreenContent: @Composable () -> Unit,
    editorScreenContent: @Composable (Day, Month, Year) -> Unit
) {
    NavHost(navController = navController, startDestination = AppScreen.Calendar.route) {
        composable(route = AppScreen.Calendar.route) {
            calendarScreenContent()
        }
        composable(
            route = AppScreen.EventEditor.route,
            arguments = listOf(navArgument(AppScreen.EventEditor.DATE_PARAM) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val rawDate =
                backStackEntry.arguments?.getString(AppScreen.EventEditor.DATE_PARAM) ?: ""

            with(rawDate) {
                val delimiter = '/'
                val day = substringBefore(delimiter)
                val month = substringAfter(delimiter).substringBefore(delimiter)
                val year = substringAfterLast(delimiter)
                editorScreenContent(Day(day.toInt()), Month(month.toInt()), Year(year.toInt()))
            }
        }
    }
}