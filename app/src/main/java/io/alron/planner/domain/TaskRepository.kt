package io.alron.planner.domain

import java.time.LocalDate

interface TaskRepository {
    suspend fun getTasksForDay(date: LocalDate): List<Task>
    suspend fun getTaskById(id: Int): Task

    suspend fun addTask(task: Task)
    suspend fun deleteTask(id: Int)
}