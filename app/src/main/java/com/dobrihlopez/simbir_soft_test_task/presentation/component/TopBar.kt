package com.dobrihlopez.simbir_soft_test_task.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dobrihlopez.simbir_soft_test_task.app.theme.LocalSpacing
import com.dobrihlopez.simbir_soft_test_task.app.theme.Simbir_soft_test_taskTheme

@Composable
fun TopBar(
    titleText: String,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    startSlot: @Composable (BoxScope.() -> Unit)? = null
) {
    val spacing = LocalSpacing.current
    Box(
        modifier = modifier
            .height(40.dp)
            .fillMaxWidth()
//            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        startSlot?.invoke(this)
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = spacing.spaceMedium),
            maxLines = 1,
            text = titleText.take(20),
            style = textStyle,
            color = textColor,
        )
    }
}

@Preview
@Composable
private fun TopBar_light() {
    Simbir_soft_test_taskTheme(darkTheme = false) {
        TopBar(titleText = "Lorem ipsum") {
            BackButton(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun TopBar_dark() {
    Simbir_soft_test_taskTheme(darkTheme = true) {
        TopBar(titleText = "Lorem ipsum Lorem ipsum Lorem ipsum") {
            BackButton(
                modifier = Modifier
                    .align(Alignment.CenterStart),
                onClick = {}
            )
        }
    }
}

@Preview
@Composable
private fun TopBar_light_no_button() {
    Simbir_soft_test_taskTheme(darkTheme = false) {
        TopBar(titleText = "Lorem ipsum")
    }
}