package com.example.fokus_cuy.domain.repository

import com.example.fokus_cuy.domain.model.TaskItem
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getAllTasks(): Flow<List<TaskItem>>
    suspend fun getTaskById(id: Long): TaskItem?
    suspend fun insertTask(task: TaskItem): Long
    suspend fun updateTask(task: TaskItem)
    suspend fun deleteTask(task: TaskItem)
}
