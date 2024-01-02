package com.dobrihlopez.simbir_soft_test_task.navigation

import com.dobrihlopez.simbir_soft_test_task.domain.util.encode

sealed class AppScreen(val route: String) {
    data object Calendar : AppScreen(CALENDAR_ROUTE)

    data object EventEditor : AppScreen(EVENT_EDITOR_ROUTE) {
        //        private const val DATE_PATTERN = "%s/%s/%s"
        const val EVENT_ID_PARAM = "event_id"

        fun parseArguments(
            eventId: Long
        ): String {
            return EVENT_EDITOR_ROUTE
                .replace(
                    "{$EVENT_ID_PARAM}",
                    eventId.toString().encode()
//                    "21/07/2003".encode()//getData(day = day, month = month, year = year).encode()
                )
        }

//        private fun getData(day: Day, month: Month, year: Year) =
//            DATE_PATTERN.format(day.value, month.value, year.value)
    }

    companion object {
        const val CALENDAR_ROUTE = "calendar"
        const val EVENT_EDITOR_ROUTE = "event_editor?{${EventEditor.EVENT_ID_PARAM}}"
    }
}