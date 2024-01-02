package com.dobrihlopez.simbir_soft_test_task.domain.usecase

import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishDateTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartDateTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartTime

class UpdateEventDurationUseCase(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke(
        eventId: Long,
        oldDateTimeStart: StartDateTime,
        oldDateTimeFinish: FinishDateTime,
        newStartTime: StartTime,
        newFinishTime: FinishTime
    ): Result<Unit> {
        return try {
            val newStartDateTime = oldDateTimeStart
                .withHour(newStartTime.hour)
                .withMinute(newStartTime.minute)

            val newFinishDateTime = oldDateTimeFinish
                .withHour(newFinishTime.hour)
                .withMinute(newFinishTime.minute)

            repository.updateEventDuration(eventId, newStartDateTime, newFinishDateTime)
            Result.success(Unit)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}