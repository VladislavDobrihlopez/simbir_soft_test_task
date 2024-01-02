package com.dobrihlopez.simbir_soft_test_task.presentation.editorScreen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dobrihlopez.simbir_soft_test_task.R
import com.dobrihlopez.simbir_soft_test_task.app.theme.LocalSpacing
import com.dobrihlopez.simbir_soft_test_task.app.theme.spacing
import com.dobrihlopez.simbir_soft_test_task.presentation.component.BackButton
import com.dobrihlopez.simbir_soft_test_task.presentation.component.TopBar
import com.dobrihlopez.simbir_soft_test_task.presentation.formatterDate
import com.dobrihlopez.simbir_soft_test_task.presentation.formatterTime
import java.time.LocalDateTime

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
                        topBarText.value = state.extendedEvent.event.name
                        Spacer(modifier = Modifier.height(MaterialTheme.spacing.spaceMedium))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max)
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(0.15f)
                                    .fillMaxHeight()
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(MaterialTheme.shapes.small)
                                        .background(state.extendedEvent.color)
                                        .align(Alignment.CenterStart)
                                )
                            }
                            Column(
                                modifier = Modifier
                                    .weight(0.85f)
                            ) {
                                val isStartDateTouched = rememberSaveable {
                                    mutableStateOf(false)
                                }

                                val isFinishDateTouched = rememberSaveable {
                                    mutableStateOf(false)
                                }

                                with(state.extendedEvent) {
                                    Text(
                                        text = event.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = event.dateStart.format(formatterDate),
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.width(spacing.spaceMedium))

                                        TouchableItem(
                                            isTouched = isStartDateTouched.value,
                                            onNotTouchedContent = {
                                                Box(
                                                    modifier = Modifier
                                                        .padding(vertical = 2.dp, horizontal = 5.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.15f
                                                            )
                                                        )
                                                        .clickable {
                                                            isStartDateTouched.value = true
                                                        },
                                                ) {
                                                    Text(
                                                        text = event.dateStart.format(formatterTime),
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            },
                                            onTouchedContent = {

                                            })

                                        Text(text = "—")

                                        TouchableItem(
                                            isTouched = isFinishDateTouched.value,
                                            onNotTouchedContent = {
                                                Box(
                                                    modifier = Modifier
                                                        .padding(vertical = 2.dp, horizontal = 5.dp)
                                                        .clip(CircleShape)
                                                        .background(
                                                            MaterialTheme.colorScheme.onSurface.copy(
                                                                alpha = 0.15f
                                                            )
                                                        )
                                                        .clickable {
                                                            isFinishDateTouched.value = true
                                                        }
                                                ) {
                                                    Text(
                                                        text = event.dateFinish.format(formatterTime),
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        color = MaterialTheme.colorScheme.onSurface
                                                    )
                                                }
                                            },
                                            onTouchedContent = {
                                                // todo add time picker
//                                                TextField(
//                                                    modifier = Modifier.fillMaxWidth(),
//                                                    value = event.dateFinish.format(formatterTime),
//                                                    textStyle = MaterialTheme.typography.bodyLarge,
//                                                    onValueChange = {},
//                                                    shape = MaterialTheme.shapes.small,
//                                                )
                                            })
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(spacing.spaceMedium))

                        val isDescriptionTouched = rememberSaveable {
                            mutableStateOf(false)
                        }

                        TouchableItem(
                            isTouched = isDescriptionTouched.value,
                            onNotTouchedContent = {
                                Box(modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(MaterialTheme.shapes.small)
                                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f))
                                    .clickable { isDescriptionTouched.value = true }
                                    .padding(spacing.spaceMedium)
                                ) {
                                    Text(
                                        text = state.extendedEvent.event.description,
                                        style = MaterialTheme.typography.bodyMedium,
                                    )
                                }
                            },
                            onTouchedContent = {
                                TextField(
                                    modifier = Modifier.fillMaxWidth(),
                                    value = state.extendedEvent.event.description,
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    onValueChange = {},
                                    shape = MaterialTheme.shapes.small,
                                )
                            })

                        Spacer(modifier = Modifier.height(spacing.spaceMedium))

                        Button(
                            modifier = Modifier.align(Alignment.End),
                            onClick = { },
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
                }
            }
        }
    }
}

@Composable
inline fun TouchableItem(
    isTouched: Boolean,
    crossinline onNotTouchedContent: @Composable () -> Unit,
    crossinline onTouchedContent: @Composable () -> Unit
) {
    AnimatedContent(targetState = isTouched, label = "") { isActive ->
        if (isActive) onTouchedContent() else onNotTouchedContent()
    }
}