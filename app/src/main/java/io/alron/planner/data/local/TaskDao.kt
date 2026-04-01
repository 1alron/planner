package io.alron.planner.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks WHERE date_start < :endOfDay AND date_finish > :startOfDay")
    suspend fun getTasksForDay(startOfDay: Long, endOfDay: Long): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskById(id: Int): TaskEntity?

    @Query("SELECT * FROM tasks WHERE date_start < :newEnd AND date_finish > :newStart")
    suspend fun getConflictingTasks(newStart: Long, newEnd: Long): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(taskEntity: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun delete(id: Int)
}