package com.dobrihlopez.simbir_soft_test_task.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.BUFFERED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<STATE, EVENT, SIDE_EFFECT>(
    private val initialState: STATE
) : ViewModel() {
    // for one-time events
    open val sideEffects = Channel<SIDE_EFFECT>(BUFFERED)

    // for state of screen
    protected open val _state = MutableStateFlow(initialState)
    open val state = _state.asStateFlow()

    // for incoming events from the user
    abstract fun onEvent(event: EVENT)
}