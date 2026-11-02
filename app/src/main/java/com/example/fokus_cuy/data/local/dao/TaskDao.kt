package com.example.fokus_cuy.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.fokus_cuy.data.local.entity.TaskEntity
import com.example.fokus_cuy.data.local.entity.relation.TaskWithSubtasks
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Transaction
    @Query(
        """
        SELECT * FROM tasks 
        WHERE user_id = :userId 
          AND scheduled_date = :scheduledDate 
          AND deleted_at IS NULL 
        ORDER BY created_at ASC
        """
    )
    fun observeTasksByDate(userId: String, scheduledDate: String): Flow<List<TaskWithSubtasks>>

    @Transaction
    @Query(
        """
        SELECT * FROM tasks 
        WHERE user_id = :userId 
          AND quadrant = :quadrant 
          AND deleted_at IS NULL 
        ORDER BY created_at ASC
        """
    )
    fun observeTasksByQuadrant(userId: String, quadrant: String): Flow<List<TaskWithSubtasks>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId AND deleted_at IS NULL LIMIT 1")
    fun observeTaskById(taskId: String): Flow<TaskWithSubtasks?>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :taskId AND deleted_at IS NULL LIMIT 1")
    suspend fun getTaskById(taskId: String): TaskWithSubtasks?

    @Upsert
    suspend fun upsertTask(task: TaskEntity)

    @Query(
        """
        UPDATE tasks 
        SET deleted_at = :deletedAt, updated_at = :updatedAt 
        WHERE id = :taskId
        """
    )
    suspend fun softDeleteTask(taskId: String, deletedAt: Long, updatedAt: Long)

    @Query(
        """
        UPDATE tasks 
        SET is_completed = :isCompleted, completed_at = :completedAt, updated_at = :updatedAt 
        WHERE id = :taskId
        """
    )
    suspend fun updateTaskCompletion(
        taskId: String,
        isCompleted: Boolean,
        completedAt: Long?,
        updatedAt: Long
    )
}
