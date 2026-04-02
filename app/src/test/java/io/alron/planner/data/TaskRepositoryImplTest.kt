package io.alron.planner.data

import io.alron.planner.data.local.TaskDao
import io.alron.planner.data.local.TaskEntity
import io.alron.planner.domain.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.*

import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class TaskRepositoryImplTest {

    private lateinit var dao: TaskDao
    private lateinit var repository: TaskRepositoryImpl

    private val zone = ZoneId.systemDefault()

    @Before
    fun setup() {
        dao = mock()
        repository = TaskRepositoryImpl(dao)
    }

    @Test
    fun `getTasksForDay returns tasks mapped to domain`() = runTest {
        val today = LocalDate.now()
        val start = today.atStartOfDay(zone).toInstant().toEpochMilli()
        val end = today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()

        val entity = TaskEntity(
            id = 1,
            name = "Тест",
            description = "Описание",
            date_start = start,
            date_finish = end
        )

        whenever(dao.getTasksForDay(start, end)).thenReturn(listOf(entity))

        val tasks = repository.getTasksForDay(today)

        assert(tasks.size == 1)
        assert(tasks[0].id == 1)
        assert(tasks[0].name == "Тест")

        verify(dao).getTasksForDay(start, end)
    }

    @Test
    fun `addTask inserts task when no conflicts`() = runTest {
        val task = Task(
            id = 0,
            name = "Задача",
            description = "Описание",
            dateStart = 1000L,
            dateFinish = 2000L
        )

        whenever(
            dao.getConflictingTasks(
                task.dateStart,
                task.dateFinish
            )
        ).thenReturn(emptyList())

        repository.addTask(task)

        verify(dao).insert(task.toEntity())
    }

    @Test(expected = IllegalStateException::class)
    fun `addTask throws when conflicts exist`() = runTest {
        val task = Task(
            id = 0,
            name = "Задача",
            description = "Описание",
            dateStart = 1000L,
            dateFinish = 2000L
        )

        whenever(
            dao.getConflictingTasks(
                task.dateStart,
                task.dateFinish
            )
        )
            .thenReturn(listOf(mock<TaskEntity>()))

        repository.addTask(task)
    }
}