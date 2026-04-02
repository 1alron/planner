package io.alron.planner.presentation.calendar_and_tasks

import io.alron.planner.domain.Task

data class TimeSlot(
    val hour: Int,
    val task: Task?,
    val duration: Int
)
