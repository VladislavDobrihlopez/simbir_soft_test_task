package com.dobrihlopez.simbir_soft_test_task.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class Event(
    val id: Long,
    val name: String,
    val description: String,
    val dateStart: LocalDateTime,
    val dateFinish: LocalDateTime,
): Parcelable