package com.dobrihlopez.simbir_soft_test_task.domain.util

import android.os.Looper

private val Thread.isOnUiThread: Boolean
    get() {
        return Looper.myLooper() == Looper.getMainLooper()
    }

fun Thread.assertOnUiThread() {
    if (!isOnUiThread) {
        throw IllegalStateException("This code must be executed on the ui thread")
    }
}

fun Thread.assertNotOnUiThread() {
    if (isOnUiThread) {
        throw IllegalStateException("This code mustn't be executed on the ui thread")
    }
}
