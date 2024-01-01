package com.dobrihlopez.simbir_soft_test_task.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dobrihlopez.simbir_soft_test_task.R
import com.dobrihlopez.simbir_soft_test_task.app.theme.Simbir_soft_test_taskTheme

@Composable
fun BackButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .background(MaterialTheme.colorScheme.secondary)
            .clickable(onClick = onClick)
            .padding(10.dp)
    ) {
        Icon(
            tint = MaterialTheme.colorScheme.onSecondary,
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = stringResource(R.string.navigate_back)
        )
    }
}

@Preview
@Composable
private fun PreviewNavBackButton_light() {
    Simbir_soft_test_taskTheme(darkTheme = false) {
        BackButton(onClick = {})
    }
}

@Preview
@Composable
private fun PreviewNavBackButton_dark() {
    Simbir_soft_test_taskTheme(darkTheme = true) {
        BackButton(onClick = {})
    }
}