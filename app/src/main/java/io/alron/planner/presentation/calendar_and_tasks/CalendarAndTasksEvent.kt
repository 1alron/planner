package io.alron.planner.presentation.calendar_and_tasks

import java.time.LocalDate

sealed interface CalendarAndTasksEvent {
    data class TaskAdded(val date: LocalDate) : CalendarAndTasksEvent
    data object TaskDeleted: CalendarAndTasksEvent
    data class Error(val message: String) : CalendarAndTasksEvent
}