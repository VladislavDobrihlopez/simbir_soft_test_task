package com.dobrihlopez.simbir_soft_test_task.domain.usecase

import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event

class UpdateEventUseCase(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(event: Event): Result<Unit> {
        return try {
            repository.updateEvent(event = event)
            Result.success(Unit)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}