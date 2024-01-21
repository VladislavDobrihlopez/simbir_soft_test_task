package com.dobrihlopez.simbir_soft_test_task.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [EventDbEntity::class], version = 1)
@TypeConverters(value = [EventConverterHolder::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventsDao(): EventsDao

    companion object {
        const val DB_NAME = "duties_calendar.db"
    }
}