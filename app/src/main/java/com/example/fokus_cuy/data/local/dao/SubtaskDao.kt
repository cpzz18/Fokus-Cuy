package com.example.fokus_cuy.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.fokus_cuy.data.local.entity.SubtaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubtaskDao {

    @Query(
        """
        SELECT * FROM subtasks 
        WHERE task_id = :taskId 
          AND deleted_at IS NULL 
        ORDER BY order_index ASC
        """
    )
    fun observeSubtasksByTaskId(taskId: String): Flow<List<SubtaskEntity>>

    @Query(
        """
        SELECT * FROM subtasks 
        WHERE task_id = :taskId 
          AND deleted_at IS NULL 
        ORDER BY order_index ASC
        """
    )
    suspend fun getSubtasksByTaskId(taskId: String): List<SubtaskEntity>

    @Query("SELECT * FROM subtasks WHERE id = :subtaskId AND deleted_at IS NULL LIMIT 1")
    suspend fun getSubtaskById(subtaskId: String): SubtaskEntity?

    @Upsert
    suspend fun upsertSubtask(subtask: SubtaskEntity)

    @Upsert
    suspend fun upsertSubtasks(subtasks: List<SubtaskEntity>)

    @Query(
        """
        UPDATE subtasks 
        SET is_completed = :isCompleted, completed_at = :completedAt, updated_at = :updatedAt 
        WHERE id = :subtaskId
        """
    )
    suspend fun setSubtaskCompleted(
        subtaskId: String,
        isCompleted: Boolean,
        completedAt: Long?,
        updatedAt: Long
    )

    @Query(
        """
        UPDATE subtasks 
        SET deleted_at = :deletedAt, updated_at = :updatedAt 
        WHERE id = :subtaskId
        """
    )
    suspend fun softDeleteSubtask(subtaskId: String, deletedAt: Long, updatedAt: Long)

    @Query(
        """
        UPDATE subtasks 
        SET deleted_at = :deletedAt, updated_at = :updatedAt 
        WHERE task_id = :taskId AND deleted_at IS NULL
        """
    )
    suspend fun softDeleteSubtasksByTaskId(taskId: String, deletedAt: Long, updatedAt: Long)
}
