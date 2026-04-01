package io.alron.planner.presentation.task_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.alron.planner.domain.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(
    private val repository: TaskRepository
) : ViewModel() {
    private val _state = MutableStateFlow<TaskDetailsState>(TaskDetailsState.Loading)
    val state = _state.asStateFlow()

    fun getTask(taskId: Int) {
        _state.update {
            TaskDetailsState.Loading
        }
        runCatching {
            viewModelScope.launch {
                val task = repository.getTaskById(taskId)
                _state.update {
                    TaskDetailsState.Content(task)
                }
            }
        }.onFailure { throwable ->
            _state.update {
                TaskDetailsState.Error(throwable.message ?: "Неизвестная ошибка")
            }
        }
    }
}