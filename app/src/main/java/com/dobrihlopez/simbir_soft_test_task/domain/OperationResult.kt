package com.dobrihlopez.simbir_soft_test_task.domain

sealed class OperationResult<out T> {
    data class Success<T>(val data: T) : OperationResult<T>()
    data class Error<T, E: Throwable>(val throwable: E, val data: T? = null) : OperationResult<T>()
}