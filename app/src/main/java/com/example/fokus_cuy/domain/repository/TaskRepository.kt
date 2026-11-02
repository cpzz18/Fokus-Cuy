package com.example.fokus_cuy.domain.repository

import com.example.fokus_cuy.domain.model.Quadrant
import com.example.fokus_cuy.domain.model.Subtask
import com.example.fokus_cuy.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun observeTasksByDate(userId: String, date: String): Flow<List<Task>>
    fun observeTasksByQuadrant(userId: String, quadrant: Quadrant): Flow<List<Task>>
    fun getTaskById(taskId: String): Flow<Task?>
    suspend fun upsertTask(task: Task)
    suspend fun deleteTask(taskId: String)
    suspend fun toggleSubtask(subtaskId: String, isCompleted: Boolean)
    suspend fun upsertSubtask(subtask: Subtask)
    suspend fun deleteSubtask(subtaskId: String)
}
