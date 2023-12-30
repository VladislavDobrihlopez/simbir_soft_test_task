package com.dobrihlopez.simbir_soft_test_task.core.util

import android.net.Uri

fun String.encode() = Uri.encode(this)

fun String.decode() = Uri.decode(this)
