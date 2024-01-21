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
    editorScreenContent: @Composable (Long?, String?) -> Unit
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
            }, navArgument(AppScreen.EventEditor.EVENT_ITEM_COLOR) {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val eventId =
                backStackEntry.arguments?.getString(AppScreen.EventEditor.EVENT_ID_PARAM)?.toLong()
            val color = backStackEntry.arguments?.getString(AppScreen.EventEditor.EVENT_ITEM_COLOR)
//            val eventId = rawEventId.toLong()
            editorScreenContent(eventId, color)
//            with(rawEventId) {
//                val delimiter = '/'
//                val day = substringBefore(delimiter)
//                val month = substringAfter(delimiter).substringBefore(delimiter)
//                val year = substringAfterLast(delimiter)
//                editorScreenContent(Day(day.toInt()), Month(month.toInt()), Year(year.toInt()))
//            }
        }
        composable(route = AppScreen.EventCreator.route) {
            editorScreenContent(null, null)
        }
    }
}