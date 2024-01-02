package com.dobrihlopez.simbir_soft_test_task.presentation

import android.content.Context
import androidx.annotation.StringRes

sealed interface UiText {
    data class Resource(@StringRes val stringResId: Int) : UiText
    data class Runtime(val message: String) : UiText

    fun getValue(context: Context) =
        when (this) {
            is Resource -> context.getString(stringResId)
            is Runtime -> message
        }
}