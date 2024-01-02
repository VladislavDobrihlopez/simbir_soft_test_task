package com.dobrihlopez.simbir_soft_test_task.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishDateTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartDateTime
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Query("SELECT * FROM events")
    fun getEvents(): Flow<List<EventDbEntity>>

    @Query("SELECT * FROM events WHERE id=:eventId LIMIT 1")
    suspend fun getDetailedEvent(eventId: Long): EventDbEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEvent(event: EventDbEntity)

    @Query(
        """
        UPDATE events SET start_date = :newStartDateTime, finish_date = :newFinishDateTime WHERE id = :eventId
        """
    )
    suspend fun updateEventDuration(
        eventId: Long,
        newStartDateTime: StartDateTime,
        newFinishDateTime: FinishDateTime
    )

    @Query("DELETE FROM events WHERE id=:eventId")
    suspend fun deleteEvent(eventId: Long)
}