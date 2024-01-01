package com.dobrihlopez.simbir_soft_test_task.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity("events")
data class EventDbEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    @ColumnInfo("name")
    val name: String,
    @ColumnInfo("description")
    val description: String,
    @ColumnInfo("start_date")
    val dateStart: LocalDateTime,
    @ColumnInfo("finish_date")
    val dateFinish: LocalDateTime,
)