package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.dobrihlopez.simbir_soft_test_task.domain.model.Event
import com.dobrihlopez.simbir_soft_test_task.presentation.model.EventUiModel
import kotlin.math.roundToInt

@JvmInline
value class ScrollPos(val value: Float)

@JvmInline
value class VisibleItemsNumber(val value: Int)

@Composable
fun ScheduleChart(
    state: State<ScheduleChartState>,
    onComposableSizeDefined: (IntSize) -> Unit,
    onTransformationChange: (VisibleItemsNumber, ScrollPos) -> Unit,
    modifier: Modifier = Modifier
) {
    val chartState = state.value
    val scheme = MaterialTheme.colorScheme
    val alphaOnSurfaceColor = scheme.onSurface.copy(alpha = 0.33f)
    val typography = MaterialTheme.typography

    val transformableState =
        rememberTransformableState(onTransformation = { zoomChange: Float,
                                                        panChange: Offset,
                                                        _: Float ->
            val newNumberOfVisibleItems =
                ((1f / zoomChange) * chartState.visibleHourBlocks)
                    .roundToInt()
                    .coerceAtLeast(ScheduleChartState.DEFAULT_MIN_NUMBER_OF_VISIBLE_HOURS)
                    .coerceAtMost(ScheduleChartState.DEFAULT_MAX_NUMBER_OF_VISIBLE_HOURS)

            val newScrollPos =
                (chartState.scrolledYaxis + panChange.y)
                    .coerceIn(
                        -(chartState.blockHeight * ScheduleChartState.BLOCKS_IN_TOTAL_IN_SCREEN
                                - chartState.componentHeight),
                        chartState.blockHeight
                    )

            onTransformationChange(
                VisibleItemsNumber(newNumberOfVisibleItems),
                ScrollPos(newScrollPos)
            )
        })

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .onSizeChanged(onComposableSizeDefined)
            .transformable(transformableState)
    ) {
        translate(top = chartState.scrolledYaxis) {
            drawLine(
                color = scheme.onSurface,
                start = Offset(
                    ScheduleChartState.TIME_COLUMN_WIDTH_IN_DP.toPx(),
                    -chartState.blockHeight
                ),
                end = Offset(
                    ScheduleChartState.TIME_COLUMN_WIDTH_IN_DP.toPx(),
                    (ScheduleChartState.BLOCKS_IN_TOTAL_IN_SCREEN + 1) * chartState.blockHeight
                )
            )

            repeat(ScheduleChartState.BLOCKS_IN_TOTAL_IN_SCREEN) { index ->
                val lineStartPositionY = index * chartState.blockHeight

                val result = textMeasurer.measure(
                    text = "$index:00",
                    style = typography.bodyMedium
                )

                drawLine(
                    color = alphaOnSurfaceColor,
                    start = Offset(result.size.width.toFloat(), lineStartPositionY),
                    end = Offset(chartState.componentWidth, lineStartPositionY),
                    cap = StrokeCap.Round
                )

                drawText(
                    result,
                    scheme.onSurface,
                    Offset(0f, lineStartPositionY - result.size.height / 2)
                )
            }
            // hour column
            chartState.visibleHourBlocksModels.forEachIndexed { index, event ->
                val startYPos = (event.dateStart.hour + event.dateStart.minute / 60f) * chartState.blockHeight
                val finishYPos = (event.dateFinish.hour + event.dateFinish.minute / 60f) * chartState.blockHeight
                val startXMargin = ScheduleChartState.TIME_COLUMN_WIDTH_IN_DP.toPx()
                val size = Size(chartState.componentWidth, (finishYPos - startYPos))
                drawRoundRect(
                    color = event.color,
                    topLeft = Offset(startXMargin, startYPos),
                    size = size,
                    cornerRadius = CornerRadius(25f, 25f)
                )
            }
        }
    }
}

data class ScheduleChartState(
    val events: List<EventUiModel>,
    val scrolledYaxis: Float = 0f,
    val componentSize: IntSize = IntSize(0, 0),
    val visibleHourBlocks: Int = DEFAULT_NUMBER_OF_VISIBLE_HOURS,
) {
    val componentWidth: Float
        get() = componentSize.width.toFloat()

    val componentHeight: Float
        get() = componentSize.height.toFloat()

    val blockHeight: Float
        get() = componentHeight / visibleHourBlocks

    val visibleHourBlocksModels: List<EventUiModel>
        get() {
            return try {
//                val startTakenEventIndex = (scrolledYaxis / blockHeight)
//                    .roundToInt()
//
//                val finalTakenEventIndex =
//                    (startTakenEventIndex + visibleHourBlocks)
//                        .coerceAtMost(events.size - 1)
//
//                events.subList(startTakenEventIndex, finalTakenEventIndex)
                events
            } catch (ex: Exception) {
                Log.d(TAG, "visibleHourBlocksModels: ${ex.message}")
                listOf()
            }
        }

    companion object {
        private val TAG = ScheduleChartState::class.java.simpleName
        const val BLOCKS_IN_TOTAL_IN_SCREEN = 24 // 24 hours a day (European 24h format time)
        const val DEFAULT_MIN_NUMBER_OF_VISIBLE_HOURS = 4
        const val DEFAULT_NUMBER_OF_VISIBLE_HOURS = 8
        const val DEFAULT_MAX_NUMBER_OF_VISIBLE_HOURS = 19
        val TIME_COLUMN_WIDTH_IN_DP = 48.dp
    }
}

@Composable
fun rememberDayScheduleChartState(events: List<EventUiModel>) = remember {
    mutableStateOf(ScheduleChartState(events))
}