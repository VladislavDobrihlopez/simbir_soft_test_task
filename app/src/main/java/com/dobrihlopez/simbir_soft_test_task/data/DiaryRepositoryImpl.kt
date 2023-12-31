package com.dobrihlopez.simbir_soft_test_task.data

import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import kotlin.properties.ObservableProperty
import kotlin.reflect.KProperty

class DiaryRepositoryImpl : DiaryRepository {
    override fun getEvents(): StateFlow<List<Event>> {
        TODO("Not yet implemented")
    }

    override suspend fun getDetailedEvent(id: Long): Event {
        TODO("Not yet implemented")
    }

    override suspend fun updateEvent(event: Event) {
        TODO("Not yet implemented")
    }

    override suspend fun createEvent(event: Event) {
        TODO("Not yet implemented")
    }
}