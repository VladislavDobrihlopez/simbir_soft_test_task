package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.dobrihlopez.simbir_soft_test_task.app.theme.LocalSpacing
import com.dobrihlopez.simbir_soft_test_task.presentation.component.BackButton
import com.dobrihlopez.simbir_soft_test_task.presentation.component.TopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreenContent(
    onNavigateBack: () -> Unit,
) {
    val spacing = LocalSpacing.current
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(
                horizontal = spacing.spaceMedium,
            ),
        topBar = {
            TopBar(
                titleText = "test",
                startSlot = {
                    BackButton(
                        modifier = Modifier
                            .align(Alignment.CenterStart),
                        onClick = onNavigateBack
                    )
                })
        }
    ) { paddingValues: PaddingValues ->

    }
}