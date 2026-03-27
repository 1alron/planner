package io.alron.planner.presentation.calendar_and_tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import io.alron.planner.R
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun CalendarAndTasksScreen(modifier: Modifier = Modifier) {
    val calendarState = rememberSelectableCalendarState()
    val selectedDate = calendarState.selectionState.selection.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {

        SelectableCalendar(
            calendarState = calendarState
        )
        Spacer(Modifier.height(28.dp))
        when (selectedDate) {
            null -> Text(
                text = stringResource(R.string.choose_day),
                style = MaterialTheme.typography.titleMedium
            )

            else -> {
                val formattedDate = selectedDate.format(
                    DateTimeFormatter.ofPattern(
                        "dd MMMM yyyy",
                        Locale.forLanguageTag("ru-RU")
                    )
                )
                Text(
                    text = stringResource(
                        R.string.task_list_for_date,
                        formattedDate
                    ),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                (8..20).forEach { hour ->
                    TimeAndTaskItem(hour = hour)
                }
            }
        }
    }
}