package com.example.fokus_cuy.data.repository

import com.example.fokus_cuy.data.local.dao.TaskDao
import com.example.fokus_cuy.data.local.entity.TaskEntity
import com.example.fokus_cuy.domain.model.TaskItem
import com.example.fokus_cuy.domain.repository.TaskRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<TaskItem>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getTaskById(id: Long): TaskItem? = withContext(Dispatchers.IO) {
        taskDao.getTaskById(id)?.toDomain()
    }

    override suspend fun insertTask(task: TaskItem): Long = withContext(Dispatchers.IO) {
        taskDao.insertTask(TaskEntity.fromDomain(task))
    }

    override suspend fun updateTask(task: TaskItem) = withContext(Dispatchers.IO) {
        taskDao.updateTask(TaskEntity.fromDomain(task))
    }

    override suspend fun deleteTask(task: TaskItem) = withContext(Dispatchers.IO) {
        taskDao.deleteTask(TaskEntity.fromDomain(task))
    }
}
