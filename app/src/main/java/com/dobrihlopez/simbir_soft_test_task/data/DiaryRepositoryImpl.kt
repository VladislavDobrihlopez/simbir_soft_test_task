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
    private var eventsSet = buildSet<Event> {
        repeat(10) {
            add(
                Event(
                    id = it.toLong(),
                    name = "event $it",
                    description = "description $it",
                    dateStart = LocalDateTime.of(2023, 12, 10, it, 30),
                    dateFinish = LocalDateTime.of(2023, 12, 10, it, 30)
                )
            )
        }
        repeat(5) {
            add(
                Event(
                    id = it.toLong(),
                    name = "event $it",
                    description = "description $it",
                    dateStart = LocalDateTime.of(2023, 5, 5, it, 0),
                    dateFinish = LocalDateTime.of(2023, 12, 10, it, 30)
                )
            )
        }
    }.toMutableSet()

    private val store = MutableStateFlow(eventsSet.toList())

    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        object : ObservableProperty<Set<Event>>(eventsSet) {
            override fun setValue(thisRef: Any?, property: KProperty<*>, value: Set<Event>) {
                super.setValue(thisRef, property, value)
                scope.launch {
                    store.emit(value.toList())
                }
            }
        }
    }


    override fun getEvents(): StateFlow<List<Event>> {
        return store
    }

    override suspend fun updateEvent(event: Event) {
        eventsSet = eventsSet.map {
            if (it.id == event.id) event else it
        }.toMutableSet()
    }

    override suspend fun createEvent(event: Event) {
        eventsSet.add(event)
    }
}