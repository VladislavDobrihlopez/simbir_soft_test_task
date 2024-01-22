package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.dobrihlopez.simbir_soft_test_task.R
import com.dobrihlopez.simbir_soft_test_task.app.theme.LocalSpacing
import com.dobrihlopez.simbir_soft_test_task.app.theme.spacing
import com.dobrihlopez.simbir_soft_test_task.presentation.component.BackButton
import com.dobrihlopez.simbir_soft_test_task.presentation.component.TopBar
import com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen.content.ColorBox
import com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen.content.DescriptionBlock
import com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen.content.EventInfo
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Composable
fun EditorScreenContent(
    state: EditorScreenState,
    onNavigateBack: () -> Unit,
    onSaveEvent: (
        name: String,
        description: String,
        dateStart: LocalDateTime,
        dateFinish: LocalDateTime
    ) -> Unit,
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val topBarText = rememberSaveable {
        mutableStateOf("...")
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        topBar = {
            TopBar(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.contentPadding)
                    .clip(MaterialTheme.shapes.small),
                titleText = topBarText.value,
                startSlot = {
                    BackButton(
                        modifier = Modifier
                            .align(Alignment.CenterStart),
                        onClick = onNavigateBack
                    )
                })
        }
    ) { paddingValues: PaddingValues ->
        Surface(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(spacing.contentPadding)
        ) {
            Column(
                modifier = Modifier
                    .padding(top = spacing.spaceMedium)
                    .fillMaxSize()
            ) {
                when (state) {
                    is EditorScreenState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(
                                    R.string.error_occurred,
                                    state.text.getValue(context)
                                ),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }

                    EditorScreenState.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    }

                    is EditorScreenState.Success -> {
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
                        val screenState = when (state) {
                            is EditorScreenState.Success.CreatorMode -> {
                                rememberEditorContentScreenState(
                                    startTime = LocalTime.now(),
                                    finishTime = LocalTime.now(),
                                    date = LocalDate.now(),
                                    description = stringResource(R.string.write_something),
                                    name = stringResource(R.string.empty_name)
                                )
                            }

                            is EditorScreenState.Success.EditMode -> {
                                rememberEditorContentScreenState(
                                    startTime = state.event.dateStart.toLocalTime(),
                                    finishTime = state.event.dateFinish.toLocalTime(),
                                    date = state.event.dateStart.toLocalDate(),
                                    description = state.event.description,
                                    name = state.event.name,
                                    eventColor = state.detailedEvent.color
                                )
                            }
                        }
                        SuccessScreenState(
                            state = screenState.value,
                            onTouchName = {

                            },
                            onTouchDescription = {

                            },
                            onChangeDescriptionText = {

                            },
                            onChangeName = {

                            },
                            onStartTimeTouch = {

                            },
                            onFinishTimeTouch = {

                            },
                            onNewStartTime = {

                            },
                            onNewFinishTime = {

                            },
                            onSaveEvent = {}//onSaveEvent
                        )
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ColumnScope.SuccessScreenState(
    state: ContentState,
    onTouchName: () -> Unit,
    onTouchDescription: () -> Unit,
    onChangeDescriptionText: (String) -> Unit,
    onChangeName: (String) -> Unit,
    onStartTimeTouch: () -> Unit,
    onFinishTimeTouch: () -> Unit,
    onNewStartTime: (LocalTime) -> Unit,
    onNewFinishTime: (LocalTime) -> Unit,
    onSaveEvent: () -> Unit
) {
    val spacing = LocalSpacing.current
    val startPicker = rememberTimePickerState()
    val finishPicker = rememberTimePickerState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Max)
    ) {
        ColorBox(color = state.color)
        EventInfo(
            name = state.name,
            date = state.date,
            startTime = state.startTime,
            finishTime = state.finishTime,
            isStartTimeTouched = state.isStartTimeWrapped,
            isFinishTimeTouched = state.isFinishTimeWrapped,
            startTimePickerState = startPicker,
            finishTimePickerState = finishPicker,
            onStartTimeTouch = onStartTimeTouch,
            onFinishTimeTouch = onFinishTimeTouch,
            onNewStartTime = onNewStartTime,
            onNewFinishTime = onNewFinishTime,
            isNameTouched = state.isNameWrapped,
            onNameChange = onChangeName,
            onNameTouch = onTouchName,
        )
    }

    Spacer(modifier = Modifier.height(spacing.spaceMedium))

    DescriptionBlock(
        isWrapped = state.isDescriptionWrapped,
        text = state.description,
        onTouch = onTouchDescription,
        onChangeText = onChangeDescriptionText
    )

    Spacer(modifier = Modifier.height(spacing.spaceMedium))

    Button(
        modifier = Modifier.align(Alignment.End),
        onClick = {

        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Text(
            text = stringResource(R.string.save_event),
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
        )
    }
}

@Immutable
data class ContentState(
    val startTime: LocalTime,
    val finishTime: LocalTime,
    val date: LocalDate,
    val name: String,
    val description: String,
    val isStartTimeWrapped: Boolean = true,
    val isFinishTimeWrapped: Boolean = true,
    val isDescriptionWrapped: Boolean = true,
    val isNameWrapped: Boolean = true,
    val isDateWrapped: Boolean = true,
    val color: Color = Color.Transparent,
) {
    enum class WrappableUiComponent {
        START_TIME,
        FINISH_TIME,
        DESCRIPTION,
        NAME,
        DATE
    }

    companion object {
        val saver = listSaver<MutableState<ContentState>, Any>(save = {
            val value = it.value
            buildList {
                add(value.isStartTimeWrapped)
                add(value.isFinishTimeWrapped)
                add(value.isDescriptionWrapped)
                add(value.isNameWrapped)
                add(value.isDateWrapped)
                add(value.color.value.toString())
                add(value.startTime.toString())
                add(value.finishTime.toString())
                add(value.date.toString())
                add(value.name)
                add(value.description)
            }.toList()
        }, restore = { stored ->
            val state = ContentState(
                isStartTimeWrapped = stored[0] as Boolean,
                isFinishTimeWrapped = stored[1] as Boolean,
                isDescriptionWrapped = stored[2] as Boolean,
                isNameWrapped = stored[3] as Boolean,
                isDateWrapped = stored[4] as Boolean,
                color = Color((stored[5] as String).toULong()),
                startTime = LocalTime.parse(stored[6] as String),
                finishTime = LocalTime.parse(stored[7] as String),
                date = LocalDate.parse(stored[8] as String),
                name = stored[9] as String,
                description = stored[10] as String,
            )
            mutableStateOf(state)
        })
    }
}

@Composable
fun rememberEditorContentScreenState(
    startTime: LocalTime,
    finishTime: LocalTime,
    date: LocalDate,
    description: String,
    name: String,
    eventColor: Color = Color.Transparent
): MutableState<ContentState> {
    return rememberSaveable(saver = ContentState.saver) {
        mutableStateOf(
            ContentState(
                startTime = startTime,
                finishTime = finishTime,
                date = date,
                name = name,
                description = description,
                color = eventColor
            )
        )
    }
}