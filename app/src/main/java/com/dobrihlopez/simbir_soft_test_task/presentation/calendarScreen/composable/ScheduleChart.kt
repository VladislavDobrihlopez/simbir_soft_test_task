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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.dobrihlopez.simbir_soft_test_task.domain.model.FinishTime
import com.dobrihlopez.simbir_soft_test_task.domain.model.StartTime
import com.dobrihlopez.simbir_soft_test_task.presentation.model.BriefEventUiModel
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.math.roundToInt

typealias ScrollPos = Float
typealias VisibleItemsNumber = Int

private typealias EventsToElevation = List<Pair<BriefEventUiModel, Int>>
private typealias ColorToOnColor = Pair<Color, Color>

@Composable
fun ScheduleChart(
    state: State<ScheduleChartState>,
    onComposableSizeDefined: (IntSize) -> Unit,
    onTransformationChange: (VisibleItemsNumber, ScrollPos) -> Unit,
    onTouchItem: (BriefEventUiModel) -> Unit,
    onUpdateItem: (BriefEventUiModel, StartTime, FinishTime) -> Unit,
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
                        ?.let(onTouchItem)
                }
            }
            .pointerInput(Unit) {
                detectDragGesturesAfterLongPress { change, dragAmount ->
                    Log.d(ScheduleChartState.TAG, "$dragAmount")

                    chartState
                        .getTouchedTopLayerItemIfPersists(change.previousPosition)
                        ?.let { item ->
                            chartState
                                .convertOffsetYChangeToTime(dragAmount.y, item)
                                ?.let { newPosition ->
                                    onUpdateItem(item, newPosition.first, newPosition.second)
                                }
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
            ScheduleChartState.calculateElevations(chartState.visibleHourBlocksModels)
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
                    val topPadding = spacing.spaceSmall.toPx()

                    if (result.size.height + topPadding < size.height) { // don't display if there is no place
                        drawText(
                            textLayoutResult = result,
                            color = chartState.getOnColor(layer),
                            topLeft = Offset(
                                startXMargin + spacing.spaceLarge.toPx(),
                                startYPos + topPadding
                            ),
                        )
                    }
                }
        }
    }
}

data class ScheduleChartState(
    val events: List<BriefEventUiModel>,
    val scrolledYaxis: Float = 0f,
    val componentSize: IntSize = IntSize(0, 0),
    val visibleHourBlocks: Int = DEFAULT_NUMBER_OF_VISIBLE_HOURS,
    val colorsForElevations: List<ColorToOnColor> = listOf(
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

    val visibleHourBlocksModels: List<BriefEventUiModel>
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
        require(layer < colorsForElevations.size)
        return colorsForElevations[layer].first
    }

    fun getOnColor(layer: Int): Color {
        require(layer < colorsForElevations.size)
        return colorsForElevations[layer].second
    }

    fun getTouchedTopLayerItemIfPersists(touchedArea: Offset): BriefEventUiModel? {
        var minTimeDiff = Float.MAX_VALUE
        var item: BriefEventUiModel? = null
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

    fun convertTimeToYPositionInChart(edgeType: EdgeType, eventUiModel: BriefEventUiModel): Float {
        return blockHeight * when (edgeType) {
            EdgeType.VERY_BEGINNING -> {
                getMinutesFromDate(eventUiModel.dateStart) / MINUTES_IN_HOUR.toFloat()
            }

            EdgeType.END -> {
                getMinutesFromDate(eventUiModel.dateFinish) / MINUTES_IN_HOUR.toFloat()
            }
        }
    }

    fun convertOffsetYChangeToTime(
        changeAmount: Float,
        eventUiModel: BriefEventUiModel
    ): Pair<StartTime, FinishTime>? {
        return try {
            val minutesToBeAdded =
                ((changeAmount / blockHeight) * MINUTES_IN_HOUR).roundToInt() // might be negative as well as positive

            val startTimeInMinutes = getMinutesFromDate(eventUiModel.dateStart) + minutesToBeAdded
            val finishTimeInMinutes = getMinutesFromDate(eventUiModel.dateFinish) + minutesToBeAdded

            val startTimeInHours = startTimeInMinutes / MINUTES_IN_HOUR
            val finishTimeInHours = finishTimeInMinutes / MINUTES_IN_HOUR

            val startTime =
                LocalTime.of(startTimeInHours, startTimeInMinutes % MINUTES_IN_HOUR)
            val finishTime =
                LocalTime.of(finishTimeInHours, finishTimeInMinutes % MINUTES_IN_HOUR)
            Pair(startTime, finishTime)
        } catch (ex: DateTimeException) {
            null
        }
    }

    companion object {
        private fun getMinutesFromDate(date: LocalDateTime): Int {
            return date.hour * MINUTES_IN_HOUR + date.minute
        }

        fun calculateElevations(events: List<BriefEventUiModel>): EventsToElevation {
            fun doesInclude(
                firstEvent: Pair<StartTime, FinishTime>,
                secondEvent: Pair<StartTime, FinishTime>
            ): Boolean {
                return firstEvent.first >= secondEvent.first && firstEvent.second < secondEvent.second
            }

            fun doesOverlap(
                firstEvent: Pair<StartTime, FinishTime>,
                secondEvent: Pair<StartTime, FinishTime>
            ): Boolean {
                return firstEvent.first >= secondEvent.first && firstEvent.first < secondEvent.second && firstEvent.second > secondEvent.second
            }

            val result = mutableListOf<Pair<BriefEventUiModel, Int>>()
            val items = events.sortedBy {
                getMinutesFromDate(it.dateFinish) - getMinutesFromDate(it.dateStart)
            }

            for (eventI in items.withIndex()) {
                var elevation = 0
                for (eventJ in eventI.index + 1 until items.size) {
                    val firstEvent = Pair(
                        eventI.value.dateStart.toLocalTime(),
                        eventI.value.dateFinish.toLocalTime()
                    )
                    val secondEvent = Pair(
                        items[eventJ].dateStart.toLocalTime(),
                        items[eventJ].dateFinish.toLocalTime()
                    )
                    if (doesInclude(
                            firstEvent = firstEvent,
                            secondEvent = secondEvent
                        ) || doesOverlap(firstEvent = firstEvent, secondEvent = secondEvent)
                    ) {
                        elevation++
                    }
                }
                result.add(Pair(eventI.value, elevation))
            }

            // ensuring tyne events are seen when they are included in big ones

            for (eventWithElevationIdx in result.indices) {
                for (eventIdx in eventWithElevationIdx + 1 until result.size) {
                    val eventI = result[eventWithElevationIdx].first
                    val eventJ = result[eventIdx].first
                    if (doesInclude(
                            firstEvent = Pair(
                                eventI.dateStart.toLocalTime(),
                                eventJ.dateFinish.toLocalTime()
                            ),
                            secondEvent = Pair(
                                eventJ.dateStart.toLocalTime(),
                                eventJ.dateFinish.toLocalTime()
                            )
                        )
                    ) {
                        result[eventWithElevationIdx] =
                            Pair(result[eventWithElevationIdx].first, result[eventIdx].second + 1)
                    }
                }
            }
            return result
        }

        val TAG = ScheduleChartState::class.java.simpleName
        val CONTENT_HORIZONTAL_PADDING = 4.dp
        const val BLOCKS_IN_TOTAL_IN_SCREEN = 25 // 24 hours a day (European 24h format time)
        private const val MINUTES_IN_HOUR = 60
        const val DEFAULT_MIN_NUMBER_OF_VISIBLE_HOURS = 4
        const val DEFAULT_NUMBER_OF_VISIBLE_HOURS = 8
        const val DEFAULT_MAX_NUMBER_OF_VISIBLE_HOURS = 19
        val TIME_COLUMN_WIDTH_IN_DP = 48.dp

        enum class EdgeType {
            VERY_BEGINNING,
            END
        }

        @Suppress("UNCHECKED_CAST")
        val saver: Saver<MutableState<ScheduleChartState>, Any> = listSaver(save = { original ->
            with(original.value) {
                listOf(
                    events,
                    scrolledYaxis,
                    componentSize.height,
                    componentSize.width,
                    visibleHourBlocks,
                    colorsForElevations,
                )
            }
        }, restore = { restored ->
            val size = IntSize(restored[2] as Int, restored[3] as Int)
            val stateValue = ScheduleChartState(
                events = restored[0] as List<BriefEventUiModel>,
                scrolledYaxis = restored[1] as Float,
                componentSize = size,
                visibleHourBlocks = restored[4] as Int,
                colorsForElevations = restored[5] as List<ColorToOnColor>
            )
            mutableStateOf(stateValue)
        })
    }
}

@Composable
fun rememberScheduleChartState(events: List<BriefEventUiModel>) =
    rememberSaveable(saver = ScheduleChartState.saver) {
        mutableStateOf(ScheduleChartState(events))
    }
