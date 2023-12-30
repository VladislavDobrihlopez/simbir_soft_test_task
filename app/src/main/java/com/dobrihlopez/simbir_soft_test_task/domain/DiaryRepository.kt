package com.dobrihlopez.simbir_soft_test_task.domain

import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate

interface DiaryRepository {
    fun getEvents(): StateFlow<List<Event>>
    suspend fun updateEvent(event: Event)
    suspend fun createEvent(event: Event)
}