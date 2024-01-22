package com.dobrihlopez.simbir_soft_test_task.domain.usecase

import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishDateTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartDateTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartTime
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

class UpdateEventDurationUseCase @Inject constructor(
    private val repository: DiaryRepository,
) {
    suspend operator fun invoke(
        eventId: Long,
        oldDateTimeStart: StartDateTime,
        oldDateTimeFinish: FinishDateTime,
        newStartTime: StartTime,
        newFinishTime: FinishTime,
    ): Result<Unit> {
        return try {
            val newStartDateTime = calculateTime(oldDateTimeStart, newStartTime)
            val newFinishDateTime = calculateTime(oldDateTimeFinish, newFinishTime)
            repository.updateEventDuration(eventId, newStartDateTime, newFinishDateTime)
            Result.success(Unit)
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    companion object {
        fun calculateTime(oldDate: LocalDateTime, newDate: LocalTime): LocalDateTime {
            return oldDate.withHour(newDate.hour).withMinute(newDate.minute)
        }
    }
}