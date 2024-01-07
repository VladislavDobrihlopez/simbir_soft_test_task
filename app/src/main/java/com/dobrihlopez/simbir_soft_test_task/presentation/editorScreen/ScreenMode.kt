package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen

enum class ScreenMode {
    EDITOR,
    CREATOR;

    val isInEditMode: Boolean
        get() = this == EDITOR
}