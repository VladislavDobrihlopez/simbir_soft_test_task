package com.dobrihlopez.simbir_soft_test_task.presentation.calendarScreen.composable

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dobrihlopez.simbir_soft_test_task.R
import com.dobrihlopez.simbir_soft_test_task.app.theme.LocalSpacing
import com.dobrihlopez.simbir_soft_test_task.app.theme.Simbir_soft_test_taskTheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun WeekSelector(
    state: State<WeekSelectorState>,
    onMoveToPreviousDate: () -> Unit,
    onMoveToNextDate: () -> Unit,
    onSelectNewDay: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectorState = state.value
    require(selectorState.weekData.size == WeekSelectorState.DAYS_IN_WEEK)
    val spacing = LocalSpacing.current
    val configuration = LocalConfiguration.current
    val orientation = configuration.orientation
    val daysToDisplay = remember {
        mutableStateOf<Int>(
            if (orientation == Configuration.ORIENTATION_PORTRAIT)
                WeekSelectorState.DAYS_TO_DISPLAY_FOR_PORTRAIT_MODE
            else
                WeekSelectorState.DAYS_TO_DISPLAY_FOR_ALBUM_MODE
        )
    }

    Row(
        modifier = modifier
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(spacing.spaceSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Arrow(
            onTouch = onMoveToPreviousDate,
            icon = Icons.Filled.ArrowBack,
            contentDescription = stringResource(
                R.string.previous_week
            )
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(
                spacing.spaceSmall,
                Alignment.CenterHorizontally
            ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            selectorState.weekData.take(daysToDisplay.value).forEach { date ->
                SelectedItem(
                    date = date,
                    isSelected = selectorState.selectedDate == date,
                    onTouch = {
                        onSelectNewDay(date)
                    })
            }
        }
        Arrow(
            onTouch = onMoveToNextDate,
            icon = Icons.Filled.ArrowForward,
            contentDescription = stringResource(R.string.next_week)
        )
    }
}

@Composable
private fun Arrow(onTouch: () -> Unit, icon: ImageVector, contentDescription: String) {
    IconButton(onClick = onTouch) {
        Icon(imageVector = icon, contentDescription = contentDescription)
    }
}

@Composable
private fun SelectedItem(date: LocalDate, onTouch: () -> Unit, isSelected: Boolean = false) {
    Column(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
            .clickable {
                onTouch()
            }
            .padding(4.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "${date.dayOfMonth}",
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onPrimaryContainer,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

data class WeekSelectorState(val weekData: List<LocalDate>, val selectedDate: LocalDate) {
    companion object {
        const val DAYS_IN_WEEK = 7
        const val DAYS_TO_DISPLAY_FOR_PORTRAIT_MODE = 5
        const val DAYS_TO_DISPLAY_FOR_ALBUM_MODE = 7
    }
}

@Composable
fun rememberWeekSelectorState(weekData: List<LocalDate>, selectedDate: LocalDate) = remember {
    mutableStateOf(WeekSelectorState(weekData, selectedDate))
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
private fun PreviewWeekSelector() {
    Simbir_soft_test_taskTheme {
        val selectedDate = LocalDate.now()

        val dates = buildList<LocalDate> {
            add(selectedDate)
            repeat(4) { index ->
                add(LocalDate.now().plusDays((index + 1).toLong()))
            }
        }

        WeekSelector(
            state = mutableStateOf(
                WeekSelectorState(
                    weekData = dates,
                    selectedDate = selectedDate
                )
            ),
            onMoveToPreviousDate = { /*TODO*/ },
            onMoveToNextDate = { /*TODO*/ },
            onSelectNewDay = {}
        )
    }
}
