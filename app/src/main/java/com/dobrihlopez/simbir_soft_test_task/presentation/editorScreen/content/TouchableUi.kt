package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen.content

import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.dobrihlopez.simbir_soft_test_task.presentation.Wrapper

@Composable
inline fun TouchableUi(
    isNotTouched: Boolean,
    crossinline onNotTouchedContent: @Composable (Modifier) -> Unit,
    crossinline onTouchedContent: @Composable () -> Unit
) {
    AnimatedContent(targetState = isNotTouched, label = "") { currentState ->
        if (currentState) onNotTouchedContent(Modifier.Wrapper()) else onTouchedContent()
    }
}
