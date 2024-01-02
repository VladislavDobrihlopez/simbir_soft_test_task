package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen

import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import com.dobrihlopez.simbir_soft_test_task.presentation.model.BriefEventUiModel

fun BriefEventUiModel.toEvent(): Event {
    return Event(
        id = this.id,
        name = this.name,
        description = "fsdf", // TODO
        dateStart = this.dateStart,
        dateFinish = this.dateFinish
    )
}