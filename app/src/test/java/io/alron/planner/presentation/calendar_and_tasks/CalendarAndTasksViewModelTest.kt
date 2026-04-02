package io.alron.planner.presentation.calendar_and_tasks

import io.alron.planner.domain.Task
import io.alron.planner.domain.TaskRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

import java.time.LocalDate

@OptIn(ExperimentalCoroutinesApi::class)
class CalendarAndTasksViewModelTest {

    private lateinit var repository: TaskRepository
    private lateinit var viewModel: CalendarAndTasksViewModel

    @Before
    fun setup() {
        repository = mock()
        viewModel = CalendarAndTasksViewModel(repository)
    }

    @Test
    fun `loadTasks updates slotsState with content`() = runTest {
        val date = LocalDate.now()
        val task = Task(
            1,
            "Тест",
            1000L,
            2000L,
            "Описание"
        )

        whenever(repository.getTasksForDay(date)).thenReturn(listOf(task))

        viewModel.loadTasks(date)

        val state = viewModel.slotsState.drop(1).first()
        assert(state is SlotState.Content)
        assert((state as SlotState.Content).slots.any { it.task?.id == 1 })
    }

    @Test
    fun `addTask emits TaskAdded event on success`() = runTest {
        val date = LocalDate.now()
        val task = Task(
            0,
            "Тест",
            1000L,
            2000L,
            "Описание"
        )

        whenever(repository.addTask(any())).thenReturn(Unit)

        viewModel.addTask(task.name, task.description, date, 0, 1)

        val event = viewModel.events.first()
        assert(event is CalendarAndTasksEvent.TaskAdded)
    }

    @Test
    fun `deleteTask emits TaskDeleted event`() = runTest {
        val date = LocalDate.now()

        whenever(repository.deleteTask(1)).thenReturn(Unit)

        viewModel.deleteTask(1, date)

        val event = viewModel.events.first()
        assert(event is CalendarAndTasksEvent.TaskDeleted)
    }
}