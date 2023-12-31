package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.dobrihlopez.simbir_soft_test_task.app.theme.LocalSpacing
import com.dobrihlopez.simbir_soft_test_task.presentation.model.EventUiModel
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.roundToInt

typealias ScrollPos = Float
typealias VisibleItemsNumber = Int

typealias StartTime = LocalTime
typealias FinishTime = LocalTime

private typealias EventsToLevel = List<Pair<EventUiModel, Int>>
private typealias ColorToOnColor = Pair<Color, Color>

@Composable
fun ScheduleChart(
    state: State<ScheduleChartState>,
    onComposableSizeDefined: (IntSize) -> Unit,
    onTransformationChange: (VisibleItemsNumber, ScrollPos) -> Unit,
    onTapItem: (EventUiModel) -> Unit,
    onUpdateItem: (EventUiModel, StartTime, FinishTime) -> Unit,
    modifier: Modifier = Modifier
) {
    val scheme = MaterialTheme.colorScheme
    val alphaOnSurfaceColor = scheme.onSurface.copy(alpha = 0.33f)
    val typography = MaterialTheme.typography
    val spacing = LocalSpacing.current
    val chartState by state

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
                newNumberOfVisibleItems,
                newScrollPos
            )
        })

    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    chartState
                        .getTouchedTopLayerItemIfPersists(offset)
                        ?.let(onTapItem)
                }
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress { change, dragAmount ->
                    Log.d(ScheduleChartState.TAG, "$dragAmount")

                    chartState
                        .getTouchedTopLayerItemIfPersists(change.previousPosition)
                        ?.let { item ->
                            val newPosition =
                                chartState.convertOffsetYChangeToTime(dragAmount.y, item)
                            onUpdateItem(item, newPosition.first, newPosition.second)
                        }
                }
            }
            .transformable(transformableState)
            .onSizeChanged(onComposableSizeDefined)
            .clipToBounds()
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
                    start = Offset(
                        result.size.width.toFloat() + 2 * ScheduleChartState.CONTENT_HORIZONTAL_PADDING.toPx(),
                        lineStartPositionY
                    ),
                    end = Offset(chartState.componentWidth, lineStartPositionY),
                    cap = StrokeCap.Round
                )

                drawText(
                    result,
                    scheme.onSurface,
                    Offset(
                        ScheduleChartState.CONTENT_HORIZONTAL_PADDING.toPx(),
                        lineStartPositionY - result.size.height / 2
                    )
                )
            }
            // hour column
            ScheduleChartState.calculateLevelsOfHovering(chartState.visibleHourBlocksModels)
                .sortedBy { it.second }
                .forEachIndexed { index, eventToLayer ->
                    val (event, layer) = eventToLayer
                    val startYPos =
                        chartState.convertTimeToYPositionInChart(
                            ScheduleChartState.Companion.EdgeType.VERY_BEGINNING,
                            event
                        )
                    val finishYPos = chartState.convertTimeToYPositionInChart(
                        ScheduleChartState.Companion.EdgeType.END,
                        event
                    )

                    val startXMargin = ScheduleChartState.TIME_COLUMN_WIDTH_IN_DP.toPx()
                    val size = Size(
                        chartState.componentWidth - startXMargin - ScheduleChartState.CONTENT_HORIZONTAL_PADDING.toPx()
                                - layer * 7.dp.toPx(),
                        (finishYPos - startYPos)
                    )

                    drawRoundRect(
                        color = chartState.getColor(layer),
                        topLeft = Offset(startXMargin, startYPos),
                        size = size,
                        cornerRadius = CornerRadius(25f, 25f),
                    )
                    drawRoundRect(
                        color = scheme.onSurface,
                        topLeft = Offset(startXMargin, startYPos),
                        size = size,
                        cornerRadius = CornerRadius(25f, 25f),
                        style = Stroke(width = 1.dp.toPx())
                    )
                    val result = textMeasurer.measure(
                        event.name,
                        typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    drawText(
                        textLayoutResult = result,
                        color = chartState.getOnColor(layer),
                        topLeft = Offset(
                            startXMargin + spacing.spaceLarge.toPx(),
                            startYPos + spacing.spaceLarge.toPx()
                        ),
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
    val colorsForLevelsOfHovering: List<ColorToOnColor> = listOf(
        Pair(Color.Green, Color.Black),
        Pair(Color.Red, Color.White),
        Pair(Color.Blue, Color.White),
        Pair(Color.Yellow, Color.Black),
    )
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

    fun getColor(layer: Int): Color {
        require(layer < colorsForLevelsOfHovering.size)
        return colorsForLevelsOfHovering[layer].first
    }

    fun getOnColor(layer: Int): Color {
        require(layer < colorsForLevelsOfHovering.size)
        return colorsForLevelsOfHovering[layer].second
    }

    fun getTouchedTopLayerItemIfPersists(touchedArea: Offset): EventUiModel? {
        var minTimeDiff = Float.MAX_VALUE
        var item: EventUiModel? = null
        events.forEachIndexed { index, eventUiModel ->
            // add somewhat caching
            val startPos = convertTimeToYPositionInChart(EdgeType.VERY_BEGINNING, eventUiModel)
            val endPos = convertTimeToYPositionInChart(EdgeType.END, eventUiModel)
            if ((touchedArea.y - scrolledYaxis) in startPos..endPos && endPos - startPos <= minTimeDiff) {
                minTimeDiff = endPos - startPos
                item = eventUiModel
            }
        }
        return item
    }

    fun convertTimeToYPositionInChart(edgeType: EdgeType, eventUiModel: EventUiModel): Float {
        return blockHeight * when (edgeType) {
            EdgeType.VERY_BEGINNING -> {
                getMinutesFromDate(eventUiModel.dateStart) / 60f
            }

            EdgeType.END -> {
                getMinutesFromDate(eventUiModel.dateFinish) / 60f
            }
        }
    }

    fun convertOffsetYChangeToTime(
        changeAmount: Float,
        eventUiModel: EventUiModel
    ): Pair<StartTime, FinishTime> {
        val minutesToBeAdded =
            ((changeAmount / blockHeight) * 60).roundToInt() // might be negative as well as positive

        val startTimeInMinutes = getMinutesFromDate(eventUiModel.dateStart) + minutesToBeAdded
        val finishTimeInMinutes = getMinutesFromDate(eventUiModel.dateFinish) + minutesToBeAdded

        val startTime =
            LocalTime.of(startTimeInMinutes / 60, startTimeInMinutes % 60)
        val finishTime =
            LocalTime.of(finishTimeInMinutes / 60, finishTimeInMinutes % 60)

        return Pair(startTime, finishTime)
    }

    companion object {
        private fun getMinutesFromDate(date: LocalDateTime): Int {
            return date.hour * 60 + date.minute
        }

        fun calculateLevelsOfHovering(events: List<EventUiModel>): EventsToLevel {
            fun doesInclude(
                firstEvent: Pair<StartTime, FinishTime>,
                secondEvent: Pair<StartTime, FinishTime>
            ): Boolean {
                return firstEvent.first >= secondEvent.first && firstEvent.second < secondEvent.second
            }

            val result = mutableListOf<Pair<EventUiModel, Int>>()
            val items = events.sortedBy {
                getMinutesFromDate(it.dateFinish) - getMinutesFromDate(it.dateStart)
            }
            for (i in items.withIndex()) {
                var level = 0
                for (j in i.index + 1 until items.size) {
                    if (doesInclude(
                            firstEvent = Pair(
                                i.value.dateStart.toLocalTime(),
                                i.value.dateFinish.toLocalTime()
                            ),
                            secondEvent = Pair(
                                items[j].dateStart.toLocalTime(),
                                items[j].dateFinish.toLocalTime()
                            )
                        )
                    ) {
                        level++
                    }
                }
                result.add(Pair(i.value, level))
            }
            return result
        }

        val TAG = ScheduleChartState::class.java.simpleName
        val CONTENT_HORIZONTAL_PADDING = 4.dp
        const val BLOCKS_IN_TOTAL_IN_SCREEN = 25 // 24 hours a day (European 24h format time)
        const val DEFAULT_MIN_NUMBER_OF_VISIBLE_HOURS = 4
        const val DEFAULT_NUMBER_OF_VISIBLE_HOURS = 8
        const val DEFAULT_MAX_NUMBER_OF_VISIBLE_HOURS = 19
        val TIME_COLUMN_WIDTH_IN_DP = 48.dp

        enum class EdgeType {
            VERY_BEGINNING,
            END
        }
    }
}

@Composable
fun rememberScheduleChartState(events: List<EventUiModel>) = remember {
    mutableStateOf(ScheduleChartState(events))
}