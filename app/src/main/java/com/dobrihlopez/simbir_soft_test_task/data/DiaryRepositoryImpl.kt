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
//            createEvent(
//                name = "Some name",
//                description = "Some description",
//                startDateTime = LocalDateTime.now().minusHours(3),
//                finishDateTime = LocalDateTime.now()
//            )

            eventsDao.getEvents().collect { events ->
                eventsFlow.emit(events.map { it.toEvent() })
            }
        }
    }

    private val eventsFlow = MutableSharedFlow<List<Event>>(replay = 1)

    private val pendingEvents = MutableSharedFlow<Unit>()

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