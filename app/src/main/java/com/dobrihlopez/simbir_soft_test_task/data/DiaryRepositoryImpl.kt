package com.dobrihlopez.simbir_soft_test_task.data

import com.dobrihlopez.simbir_soft_test_task.data.database.EventDbEntity
import com.dobrihlopez.simbir_soft_test_task.data.database.EventsDao
import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishDateTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartDateTime
import com.dobrihlopez.simbir_soft_test_task.domain.util.assertNotOnUiThread
import com.dobrihlopez.simbir_soft_test_task.ioc.DispatcherIO
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import javax.inject.Inject

class DiaryRepositoryImpl @Inject constructor(
    private val eventsDao: EventsDao,
    @DispatcherIO private val coroutineDispatcher: CoroutineDispatcher,
) : DiaryRepository {
    init {
        CoroutineScope(coroutineDispatcher).launch {
            val dataEvents = buildList {
                repeat(3) {
                    add(
                        Event(
                            id = it.toLong(),
                            name = "Some name $it",
                            description = "Some description $it",
                            dateStart = LocalDateTime.now().minusHours(it * 2L),
                            dateFinish = LocalDateTime.now().minusHours(it.toLong())
                        )
                    )
                }
            }

            dataEvents.forEach {
                createEvent(
                    name = it.name,
                    description = it.description,
                    startDateTime = it.dateStart,
                    finishDateTime = it.dateFinish
                )
            }

            eventsDao.getEvents().collect { events ->
                eventsFlow.emit(events.map { it.toEvent() })
            }
        }
    }

    private val eventsFlow = MutableSharedFlow<List<Event>>(replay = 1)

    override fun getEvents(): SharedFlow<List<Event>> {
        return eventsFlow
//        return MutableStateFlow(listOf())
    }

    override suspend fun askForCurrentEvents() {
        switchThread {
            eventsDao.getEvents().take(1).collect {
                eventsFlow.emit(it.map { it.toEvent() })
            }
        }
    }

    override suspend fun getDetailedEvent(eventId: Long): Event {
        return switchThread {
            val dbEntity = eventsDao.getDetailedEvent(eventId)
            dbEntity.toEvent()
        }
    }

    override suspend fun updateEvent(event: Event) {
        upsertEvent(event.toEventDbEntity())
    }

    override suspend fun createEvent(
        name: String,
        description: String,
        startDateTime: LocalDateTime,
        finishDateTime: LocalDateTime,
    ) {
        upsertEvent(
            EventDbEntity(
                name = name,
                description = description,
                dateStart = startDateTime,
                dateFinish = finishDateTime
            )
        )
    }

    override suspend fun deleteEvent(event: Event) {
        switchThread {
            eventsDao.deleteEvent(event.id)
        }
    }

    private suspend fun upsertEvent(event: EventDbEntity) {
        switchThread {
            eventsDao.upsertEvent(event)
        }
    }

    override suspend fun updateEventDuration(
        eventId: Long,
        startDateTime: StartDateTime,
        finishDateTime: FinishDateTime,
    ) {
        switchThread {
            eventsDao.updateEventDuration(eventId, startDateTime, finishDateTime)
        }
    }

    private suspend inline fun <T> switchThread(
        crossinline contract: suspend () -> T,
    ): T {
        return withContext(coroutineDispatcher) {
            /*
             * TODO: uncomment
             *  "Thread.currentThread().assertNotOnUiThread()"
             *  in the release version
             *
             * Might be commented out in debug and for testing
             */
            Thread.currentThread().assertNotOnUiThread()
            contract()
        }
    }
}