package com.dobrihlopez.simbir_soft_test_task.domain.usecase

import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    operator fun invoke(): StateFlow<List<Event>> {
        return repository.getEvents()
    }
}