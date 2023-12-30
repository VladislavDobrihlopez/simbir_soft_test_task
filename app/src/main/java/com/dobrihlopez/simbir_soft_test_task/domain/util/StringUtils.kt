package com.dobrihlopez.simbir_soft_test_task.domain.util

import android.net.Uri
import java.time.LocalDateTime

fun String.encode() = Uri.encode(this)

fun String.decode() = Uri.decode(this)

fun LocalDateTime.toHHMMTime(): String {
    return "${this.hour}:${this.minute}"
}