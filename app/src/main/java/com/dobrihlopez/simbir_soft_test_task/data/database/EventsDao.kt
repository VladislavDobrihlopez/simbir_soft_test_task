package com.dobrihlopez.simbir_soft_test_task.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EventsDao {
    @Query("SELECT * FROM events")
    fun getEvents(): Flow<List<EventDbEntity>>

    @Query("SELECT * FROM events WHERE id=:eventId LIMIT 1")
    suspend fun getDetailedEvent(eventId: Long): EventDbEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEvent(event: EventDbEntity) // either creates if doesn't exist or updates

    @Query("DELETE FROM events WHERE id=:eventId")
    suspend fun deleteEvent(eventId: Long)
}