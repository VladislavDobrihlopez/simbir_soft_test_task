package com.dobrihlopez.simbir_soft_test_task.data.database

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import java.time.LocalDateTime
import java.time.ZoneOffset

@ProvidedTypeConverter
class EventConverterHolder(private val zoneOffset: ZoneOffset) {
    @TypeConverter
    fun fromLocalDateTimeToTimestamp(time: LocalDateTime?): Long? {
        return time?.toEpochSecond(zoneOffset)
    }

    @TypeConverter
    fun fromTimestampToLocalDateTime(timestamp: Long?): LocalDateTime? {
        return timestamp?.let {
            LocalDateTime.ofEpochSecond(timestamp, 0, zoneOffset)
        }
    }
}