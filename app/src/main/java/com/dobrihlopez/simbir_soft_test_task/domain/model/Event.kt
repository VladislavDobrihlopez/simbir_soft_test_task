package com.dobrihlopez.simbir_soft_test_task.domain.model

import java.time.LocalDateTime

data class Event(
    val id: Long,
    val name: String,
    val description: String,
    val dateStart: LocalDateTime,
    val dateFinish: LocalDateTime,
)