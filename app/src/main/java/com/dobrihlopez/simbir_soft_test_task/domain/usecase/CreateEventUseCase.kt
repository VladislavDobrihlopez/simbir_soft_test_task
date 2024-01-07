package com.dobrihlopez.simbir_soft_test_task.domain.usecase

import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import java.time.LocalDateTime

class CreateEventUseCase(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(
        name: String,
        description: String,
        startDateTime: LocalDateTime,
        finishDateTime: LocalDateTime,
    ): Result<Unit> {
        return try {
            repository.createEvent(
                name,
                description,
                startDateTime,
                finishDateTime
            )
            Result.success(Unit)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}