package com.dobrihlopez.simbir_soft_test_task.domain

import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import kotlinx.coroutines.flow.StateFlow

interface DiaryRepository {
    fun getEvents(): StateFlow<List<Event>>
    suspend fun getDetailedEvent(id: Long): Event
    suspend fun updateEvent(event: Event)
    suspend fun createEvent(event: Event)
}