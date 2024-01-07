package com.dobrihlopez.simbir_soft_test_task.navigation

import androidx.compose.ui.graphics.Color
import com.dobrihlopez.simbir_soft_test_task.domain.util.encode

sealed class AppScreen(val route: String) {
    data object Calendar : AppScreen(CALENDAR_ROUTE)

    data object EventEditor : AppScreen(EVENT_EDITOR_ROUTE) {
        //        private const val DATE_PATTERN = "%s/%s/%s"
        const val EVENT_ID_PARAM = "event_id"
        const val EVENT_ITEM_COLOR = "color"

        fun parseArguments(
            eventId: Long,
            color: Color
        ): String {
            return EVENT_EDITOR_ROUTE
                .replace(
                    "{$EVENT_ID_PARAM}",
                    eventId.toString().encode()
                )
                .replace("{$EVENT_ITEM_COLOR}", color.value.toString().encode())
        }

//        private fun getData(day: Day, month: Month, year: Year) =
//            DATE_PATTERN.format(day.value, month.value, year.value)
    }

    data object EventCreator: AppScreen(EVENT_CREATOR) {

    }

    companion object {
        const val CALENDAR_ROUTE = "calendar"
        const val EVENT_CREATOR = "event_creator"
        const val EVENT_EDITOR_ROUTE = "event_editor?${EventEditor.EVENT_ID_PARAM}/${EventEditor.EVENT_ITEM_COLOR}"
//            "event_editor?${EventEditor.EVENT_ID_PARAM}=${EventEditor.EVENT_ID_PARAM}}/${EventEditor.EVENT_ITEM_COLOR}={${EventEditor.EVENT_ITEM_COLOR}}"
    }
}