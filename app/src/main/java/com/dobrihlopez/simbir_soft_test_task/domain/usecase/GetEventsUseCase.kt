package com.dobrihlopez.simbir_soft_test_task.domain.usecase

import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    operator fun invoke(currentDate: () -> LocalDate): Flow<List<Event>> {
        return repository.getEvents().map { events ->
            events.filter { it.dateStart.toLocalDate() == currentDate() }
        }
    }
}