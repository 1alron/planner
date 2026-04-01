package io.alron.planner.presentation.calendar_and_tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alron.planner.domain.Task
import io.alron.planner.domain.TaskRepository
import io.alron.planner.presentation.util.WORKING_FINISH_HOUR
import io.alron.planner.presentation.util.WORKING_START_HOUR
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class CalendarAndTasksViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _slotsState = MutableStateFlow<SlotState>(SlotState.Loading)
    val slotsState: StateFlow<SlotState> = _slotsState

    private val _events = MutableSharedFlow<CalendarAndTasksEvent>()
    val events: SharedFlow<CalendarAndTasksEvent> = _events

    fun loadTasks(date: LocalDate) {
        _slotsState.update { SlotState.Loading }
        viewModelScope.launch {
            runCatching {
                val tasks = repository.getTasksForDay(date)
                _slotsState.update { SlotState.Content(mapToSlots(tasks)) }
            }.onFailure { throwable ->
                _slotsState.update { SlotState.Error(throwable.message ?: "Неизвестная ошибка") }
            }
        }
    }

    fun deleteTask(id: Int, date: LocalDate) {
        viewModelScope.launch {
            runCatching {
                repository.deleteTask(id)
            }.onSuccess {
                loadTasks(date)
                _events.emit(CalendarAndTasksEvent.TaskDeleted)
            }.onFailure { throwable ->
                _events.emit(CalendarAndTasksEvent.Error(throwable.message ?: "Ошибка удаления"))
            }
        }
    }

    fun addTask(
        name: String,
        description: String,
        date: LocalDate,
        startTime: Int,
        endTime: Int,
    ) {
        viewModelScope.launch {
            runCatching {
                val zone = ZoneId.systemDefault()
                val start = date.atTime(startTime, 0).atZone(zone).toInstant().toEpochMilli()
                val end = date.atTime(endTime, 0).atZone(zone).toInstant().toEpochMilli()
                val task = Task(
                    id = 0,
                    name = name,
                    description = description,
                    dateStart = start,
                    dateFinish = end
                )
                repository.addTask(task)
            }.onSuccess {
                _events.emit(CalendarAndTasksEvent.TaskAdded(date))
            }.onFailure {
                _events.emit(CalendarAndTasksEvent.Error(it.message ?: "Неизвестная ошибка"))
            }
        }
    }

    private fun mapToSlots(tasks: List<Task>): List<TimeSlot> {
        val zone = ZoneId.systemDefault()

        val result = mutableListOf<TimeSlot>()
        val occupied = mutableSetOf<Int>()

        tasks.forEach { task ->
            val start = Instant.ofEpochMilli(task.dateStart).atZone(zone).toLocalDateTime()
            val end = Instant.ofEpochMilli(task.dateFinish).atZone(zone).toLocalDateTime()

            val duration = end.hour - start.hour

            result.add(
                TimeSlot(
                    hour = start.hour,
                    task = task,
                    duration = duration
                )
            )

            for (h in (start.hour + 1) until end.hour) {
                occupied.add(h)
            }
        }

        for (hour in WORKING_START_HOUR..<WORKING_FINISH_HOUR) {
            if (result.none { it.hour == hour } && hour !in occupied) {
                result.add(
                    TimeSlot(
                        hour = hour,
                        task = null,
                        duration = 1
                    )
                )
            }
        }
        return result.sortedBy { it.hour }
    }
}