package com.dobrihlopez.simbir_soft_test_task.data

import com.dobrihlopez.simbir_soft_test_task.data.database.EventDbEntity
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event

fun EventDbEntity.toEvent(): Event {
    return Event(
        id = this.id,
        name = this.name,
        description = this.description,
        dateStart = this.dateStart,
        dateFinish = this.dateFinish
    )
}

fun Event.toEventDbEntity(): EventDbEntity {
    return EventDbEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        dateStart = this.dateStart,
        dateFinish = this.dateFinish
    )
}