package io.alron.planner.presentation

sealed class Route(val route: String) {
    object CalendarAndTasks : Route("calendar_and_tasks")
    object TaskDetails : Route("task_details/{taskId}") {
        fun createRoute(taskId: Int) = "task_details/$taskId"
    }
}