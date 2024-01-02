package com.dobrihlopez.simbir_soft_test_task.domain.model

import java.time.LocalDateTime
import java.time.LocalTime

@JvmInline
value class Day(val value: Int)

@JvmInline
value class Month(val value: Int)

@JvmInline
value class Year(val value: Int)

typealias StartTime = LocalTime
typealias FinishTime = LocalTime
typealias StartDateTime = LocalDateTime
typealias FinishDateTime = LocalDateTime