package com.dobrihlopez.simbir_soft_test_task.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.EventId

class AppNavigator(val controller: NavController) {
    fun navigateToEditorScreen(eventId: EventId, color: Color) {
        controller.executeIfResumed {
            controller.navigate(AppScreen.EventEditor.parseArguments(eventId, color)) {
                launchSingleTop = true
            }
        }
    }

    fun navigateBack() {
        controller.executeIfResumed {
            controller.popBackStack()
        }
    }

    fun navigateToCreatorScreen() {
        controller.executeIfResumed {
            controller.navigate(AppScreen.EventCreator.route) {
                launchSingleTop = true
            }
        }
    }
}

@Composable
fun rememberAppNavigator(navController: NavController = rememberNavController()) =
    remember {
        mutableStateOf(AppNavigator(navController))
    }

fun NavController.executeIfResumed(contract: NavBackStackEntry.() -> Unit) {
    currentBackStackEntry?.run {
        if (lifecycleIsResumed()) {
            contract()
        }
    }
}

fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED