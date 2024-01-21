package com.dobrihlopez.simbir_soft_test_task.domain.usecase

import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import java.time.LocalDateTime
import javax.inject.Inject

class UpdateEventUseCase @Inject constructor(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(
        id: Long,
        name: String,
        description: String,
        startDateTime: LocalDateTime,
        finishDateTime: LocalDateTime,
    ): Result<Unit> {
        return try {
            repository.updateEvent(
                event = Event(
                    id = id,
                    name = name,
                    description = description,
                    dateStart = startDateTime,
                    dateFinish = finishDateTime
                )
            )
            Result.success(Unit)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}