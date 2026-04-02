package io.alron.planner.data

import io.alron.planner.data.local.TaskDao
import io.alron.planner.domain.Task
import io.alron.planner.domain.TaskRepository
import java.time.LocalDate
import java.time.ZoneId

class TaskRepositoryImpl(
    private val dao: TaskDao
) : TaskRepository {
    override suspend fun getTasksForDay(date: LocalDate): List<Task> {
        val zone = ZoneId.systemDefault()

        val startOfDay = date
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()

        val endOfDay = date
            .plusDays(1)
            .atStartOfDay(zone)
            .toInstant()
            .toEpochMilli()

        return dao.getTasksForDay(startOfDay, endOfDay).map { it.toDomain() }
    }

    override suspend fun getTaskById(id: Int): Task {
        return dao.getTaskById(id)?.toDomain() ?: throw NoSuchElementException("Задача не найдена")
    }

    override suspend fun addTask(task: Task) {
        val conflicts = dao.getConflictingTasks(
            newStart = task.dateStart,
            newEnd = task.dateFinish
        )

        if (conflicts.isNotEmpty()) {
            throw IllegalStateException("Временной слот уже занят")
        }

        dao.insert(task.toEntity())
    }

    override suspend fun deleteTask(id: Int) {
        dao.delete(id)
    }
}