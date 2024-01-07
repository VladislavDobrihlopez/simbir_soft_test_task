package com.dobrihlopez.simbir_soft_test_task.domain.usecase

import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event

class GetDetailedEventUseCase(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(eventId: Long): Result<Event> {
        return try {
            val event = repository.getDetailedEvent(eventId)
            Result.success(event)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}