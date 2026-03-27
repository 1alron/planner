package io.alron.planner.presentation.calendar_and_tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.boguszpawlowski.composecalendar.SelectableCalendar
import io.github.boguszpawlowski.composecalendar.rememberSelectableCalendarState

@Composable
fun CalendarAndTasksScreen(modifier: Modifier = Modifier) {
    val calendarState = rememberSelectableCalendarState()
    val selectedDate = calendarState.selectionState.selection.firstOrNull()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        SelectableCalendar(
            calendarState = calendarState
        )
    }
}