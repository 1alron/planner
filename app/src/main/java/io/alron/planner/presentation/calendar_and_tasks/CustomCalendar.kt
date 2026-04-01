package io.alron.planner.presentation.calendar_and_tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.alron.planner.R
import io.github.boguszpawlowski.composecalendar.CalendarState
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.selection.DynamicSelectionState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomCalendar(calendarState: CalendarState<DynamicSelectionState>) {
    SelectableCalendar(
        calendarState = calendarState,
        monthHeader = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        calendarState.monthState.currentMonth =
                            calendarState.monthState.currentMonth.minusMonths(1)
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.previous_month)
                    )
                }

                Text(
                    text = "${calendarState.monthState.currentMonth.month} " +
                            "${calendarState.monthState.currentMonth.year}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                IconButton(
                    onClick = {
                        calendarState.monthState.currentMonth =
                            calendarState.monthState.currentMonth.plusMonths(1)
                    }
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = stringResource(R.string.next_month)
                    )
                }
            }
        },

        dayContent = { dayState ->
            val isSelected =
                dayState.selectionState.isDateSelected(dayState.date)

            val isCurrentMonth = dayState.isFromCurrentMonth

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shadow(
                        elevation = if (isSelected) 4.dp else 0.dp,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .clickable(enabled = isCurrentMonth) {
                        dayState.selectionState.onDateSelected(dayState.date)
                    }
                    .background(
                        if (isSelected) MaterialTheme.colorScheme.primary
                        else Color.Transparent
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = dayState.date.dayOfMonth.toString(),
                    color = when {
                        isSelected -> MaterialTheme.colorScheme.onPrimary
                        !isCurrentMonth -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        else -> MaterialTheme.colorScheme.onBackground
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    )
}