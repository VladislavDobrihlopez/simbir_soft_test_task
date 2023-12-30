package com.dobrihlopez.simbir_soft_test_task.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

class AppNavigator(val controller: NavController) {
    fun navigateToEditorScreen(day: Day, month: Month, year: Year) {
        controller.executeIfResumed {
            controller.navigate(AppScreen.EventEditor.parseArguments(day, month, year)) {
                launchSingleTop = true
            }
        }
    }

    fun navigateBack() {
        controller.executeIfResumed {
            controller.popBackStack()
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