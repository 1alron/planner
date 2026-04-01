package io.alron.planner.presentation.task_details

import io.alron.planner.domain.Task

sealed interface TaskDetailsState {
    data object Loading : TaskDetailsState
    data class Error(val message: String) : TaskDetailsState
    data class Content(val task: Task) : TaskDetailsState
}