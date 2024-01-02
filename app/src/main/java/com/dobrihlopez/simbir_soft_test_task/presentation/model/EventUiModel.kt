package com.dobrihlopez.simbir_soft_test_task.presentation.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.time.LocalDateTime

@Parcelize
data class EventUiModel(
    val id: Long,
    val name: String,
    val dateStart: LocalDateTime,
    val dateFinish: LocalDateTime,
) : Parcelable