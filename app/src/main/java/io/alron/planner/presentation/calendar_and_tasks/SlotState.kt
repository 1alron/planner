package io.alron.planner.presentation.calendar_and_tasks

sealed interface SlotState {
    data object Loading : SlotState
    data class Error(val message: String) : SlotState
    data class Content(val slots: List<TimeSlot>) : SlotState
}