package com.dobrihlopez.simbir_soft_test_task.navigation

import com.dobrihlopez.simbir_soft_test_task.domain.model.Day
import com.dobrihlopez.simbir_soft_test_task.domain.model.Month
import com.dobrihlopez.simbir_soft_test_task.domain.model.Year
import com.dobrihlopez.simbir_soft_test_task.domain.util.encode

sealed class AppScreen(val route: String) {
    object Calendar : AppScreen(CALENDAR_ROUTE)

    object EventEditor : AppScreen(EVENT_EDITOR_ROUTE) {
        private const val DATE_PATTERN = "%s/%s/%s"
        const val DATE_PARAM = "date"

        fun parseArguments(day: Day, month: Month, year: Year): String {
            return EVENT_EDITOR_ROUTE
                .replace(
                    "{$DATE_PARAM}", "21/07/2003".encode()//getData(day = day, month = month, year = year).encode()
                )
        }

        private fun getData(day: Day, month: Month, year: Year) =
            DATE_PATTERN.format(day.value, month.value, year.value)
    }

    companion object {
        const val CALENDAR_ROUTE = "calendar"
        const val EVENT_EDITOR_ROUTE = "event_editor/{${EventEditor.DATE_PARAM}}"
    }
}