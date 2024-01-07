package com.dobrihlopez.simbir_soft_test_task.domain

import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishDateTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartDateTime
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

interface DiaryRepository {
    fun getEvents(): StateFlow<List<Event>>
    suspend fun getDetailedEvent(eventId: Long): Event
    suspend fun updateEvent(event: Event)
    suspend fun updateEventDuration(
        eventId: Long,
        startDateTime: StartDateTime,
        finishDateTime: FinishDateTime
    )

    suspend fun createEvent(
        name: String,
        description: String,
        startDateTime: LocalDateTime,
        finishDateTime: LocalDateTime,
    )

    suspend fun deleteEvent(event: Event)
}