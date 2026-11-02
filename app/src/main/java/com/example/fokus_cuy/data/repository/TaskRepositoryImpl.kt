package com.example.fokus_cuy.data.repository

import androidx.room.withTransaction
import com.example.fokus_cuy.data.local.AppDatabase
import com.example.fokus_cuy.data.local.mapper.toDomainModel
import com.example.fokus_cuy.data.local.mapper.toEntity
import com.example.fokus_cuy.domain.model.Quadrant
import com.example.fokus_cuy.domain.model.Subtask
import com.example.fokus_cuy.domain.model.Task
import com.example.fokus_cuy.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.util.UUID

class TaskRepositoryImpl(
    private val database: AppDatabase,
    private val clock: Clock = Clock.systemDefaultZone(),
    private val idGenerator: () -> String = { UUID.randomUUID().toString() }
) : TaskRepository {

    private val taskDao = database.taskDao()
    private val subtaskDao = database.subtaskDao()

    override fun observeTasksByDate(userId: String, date: String): Flow<List<Task>> {
        return taskDao.observeTasksByDate(userId, date).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun observeTasksByQuadrant(userId: String, quadrant: Quadrant): Flow<List<Task>> {
        return taskDao.observeTasksByQuadrant(userId, quadrant.name).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override fun getTaskById(taskId: String): Flow<Task?> {
        return taskDao.observeTaskById(taskId).map { it?.toDomainModel() }
    }

    override suspend fun hasAnyTask(userId: String): Boolean {
        return taskDao.hasAnyTask(userId)
    }

    override suspend fun upsertTask(task: Task) {
        database.withTransaction {
            val now = clock.millis()
            val targetTask = if (task.id.isBlank()) task.copy(id = idGenerator()) else task
            val taskEntity = targetTask.toEntity().copy(updatedAt = now)
            taskDao.upsertTask(taskEntity)

            if (targetTask.subtasks.isNotEmpty()) {
                val subtaskEntities = targetTask.subtasks.mapIndexed { index, subtask ->
                    val subtaskId = if (subtask.id.isBlank()) idGenerator() else subtask.id
                    subtask.toEntity().copy(
                        id = subtaskId,
                        taskId = targetTask.id,
                        orderIndex = index,
                        updatedAt = now
                    )
                }
                subtaskDao.upsertSubtasks(subtaskEntities)
                taskDao.syncTaskCompletionFromSubtasks(targetTask.id, now)
            }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        database.withTransaction {
            val now = clock.millis()
            taskDao.softDeleteTask(taskId, deletedAt = now, updatedAt = now)
            subtaskDao.softDeleteSubtasksByTaskId(taskId, deletedAt = now, updatedAt = now)
        }
    }

    override suspend fun toggleTaskCompletion(taskId: String, isCompleted: Boolean) {
        val now = clock.millis()
        taskDao.updateTaskCompletion(
            taskId = taskId,
            isCompleted = isCompleted,
            completedAt = if (isCompleted) now else null,
            updatedAt = now
        )
    }

    override suspend fun toggleSubtask(subtaskId: String, isCompleted: Boolean) {
        database.withTransaction {
            val subtask = subtaskDao.getSubtaskById(subtaskId) ?: return@withTransaction
            val now = clock.millis()
            subtaskDao.setSubtaskCompleted(
                subtaskId = subtaskId,
                isCompleted = isCompleted,
                completedAt = if (isCompleted) now else null,
                updatedAt = now
            )

            // Sinkronisasi atomik single-query EXISTS/NOT EXISTS (F5 & Q3)
            taskDao.syncTaskCompletionFromSubtasks(subtask.taskId, now)
        }
    }

    override suspend fun upsertSubtask(subtask: Subtask) {
        database.withTransaction {
            val now = clock.millis()
            val targetSubtask = if (subtask.id.isBlank()) subtask.copy(id = idGenerator()) else subtask
            subtaskDao.upsertSubtask(targetSubtask.toEntity().copy(updatedAt = now))
            taskDao.syncTaskCompletionFromSubtasks(targetSubtask.taskId, now)
        }
    }

    override suspend fun deleteSubtask(subtaskId: String) {
        database.withTransaction {
            val subtask = subtaskDao.getSubtaskById(subtaskId) ?: return@withTransaction
            val now = clock.millis()
            subtaskDao.softDeleteSubtask(subtaskId, deletedAt = now, updatedAt = now)
            taskDao.syncTaskCompletionFromSubtasks(subtask.taskId, now)
        }
    }
}
