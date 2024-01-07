package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.dobrihlopez.simbir_soft_test_task.R
import com.dobrihlopez.simbir_soft_test_task.app.theme.LocalSpacing
import com.dobrihlopez.simbir_soft_test_task.presentation.formatterDate
import com.dobrihlopez.simbir_soft_test_task.presentation.formatterTime
import java.time.LocalDate
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowScope.EventInfo(
    name: String,
    date: LocalDate,
    startTime: LocalTime,
    finishTime: LocalTime,
    isNameTouched: Boolean,
    isStartTimeTouched: Boolean,
    isFinishTimeTouched: Boolean,
    startTimePickerState: TimePickerState,
    finishTimePickerState: TimePickerState,
    onNameChange: (String) -> Unit,
    onNameTouch: () -> Unit,
    onStartTimeTouch: () -> Unit,
    onFinishTimeTouch: () -> Unit,
    onNewStartTime: (LocalTime) -> Unit,
    onNewFinishTime: (LocalTime) -> Unit
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = Modifier.weight(0.85f)
    ) {
        TouchableUi(
            isNotTouched = isNameTouched,
            onNotTouchedContent = {
                WrappedText(
                    modifier =
                    Modifier
                        .clickable(onClick = onNameTouch)
                        .then(it),
                    text = name
                )
            },
            onTouchedContent = {
                BasicTextField(
                    singleLine = true,
                    value = name,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .clip(MaterialTheme.shapes.small),
                    textStyle = MaterialTheme.typography.bodyMedium,
                    onValueChange = onNameChange,
                )
            })

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = date.format(formatterDate),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.width(spacing.spaceMedium))

            WrappableTime(
                time = startTime,
                isStartDateTouched = isStartTimeTouched,
                pickerState = startTimePickerState,
                onTouch = onStartTimeTouch
            )

            Text(text = stringResource(R.string.divider))

            WrappableTime(
                time = finishTime,
                isStartDateTouched = isFinishTimeTouched,
                pickerState = finishTimePickerState,
                onTouch = onFinishTimeTouch
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WrappableTime(
    time: LocalTime,
    isStartDateTouched: Boolean,
    pickerState: TimePickerState,
    onTouch: () -> Unit
) {
    TouchableUi(
        isNotTouched = isStartDateTouched,
        onNotTouchedContent = {
            WrappedText(
                modifier = Modifier
                    .clickable(onClick = onTouch)
                    .then(it),
                text = time.format(formatterTime)
            )
        },
        onTouchedContent = {
            TimePicker(state = pickerState)
        })
}