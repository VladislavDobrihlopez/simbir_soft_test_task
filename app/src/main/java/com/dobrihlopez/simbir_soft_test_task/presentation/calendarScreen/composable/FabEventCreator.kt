package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dobrihlopez.simbir_soft_test_task.R

@Composable
fun FabEventCreator(
    onTouch: () -> Unit,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onContainerColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
) {
    Box(
        modifier = Modifier
            .clip(MaterialTheme.shapes.large)
            .background(containerColor)
            .clickable { onTouch() }
            .padding(16.dp),
    ) {
        Icon(
            tint = onContainerColor,
            imageVector = Icons.Filled.Add,
            contentDescription = stringResource(R.string.add_a_new_event)
        )
    }
}