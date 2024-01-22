package com.dobrihlopez.simbir_soft_test_task.domain.usecase

import com.dobrihlopez.simbir_soft_test_task.domain.DiaryRepository
import javax.inject.Inject

class RequestEvents @Inject constructor(
    private val repository: DiaryRepository
) {
    suspend operator fun invoke() {
        repository.askForCurrentEvents()
    }
}