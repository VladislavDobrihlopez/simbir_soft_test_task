package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

@Composable
fun DescriptionBlock(
    isWrapped: Boolean,
    text: String,
    onTouch: () -> Unit,
    onChangeText: (String) -> Unit
) {
    TouchableUi(
        isNotTouched = isWrapped,
        onNotTouchedContent = {
            WrappedText(modifier = Modifier.fillMaxWidth().clickable(onClick = onTouch).then(it), text = text)
        },
        onTouchedContent = {
            BasicTextField(
                value = text,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.small),
                textStyle = MaterialTheme.typography.bodyMedium,
                onValueChange = onChangeText,
            )
        })
}