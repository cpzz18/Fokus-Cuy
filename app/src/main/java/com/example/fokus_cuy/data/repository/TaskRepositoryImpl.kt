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

    override suspend fun upsertTask(task: Task) {
        database.withTransaction {
            val now = clock.millis()
            val targetTask = if (task.id.isBlank()) task.copy(id = idGenerator()) else task
            val taskEntity = targetTask.toEntity().copy(updatedAt = now)
            taskDao.upsertTask(taskEntity)

            targetTask.subtasks.forEachIndexed { index, subtask ->
                val subtaskId = if (subtask.id.isBlank()) idGenerator() else subtask.id
                val subtaskEntity = subtask.toEntity().copy(
                    id = subtaskId,
                    taskId = targetTask.id,
                    orderIndex = index,
                    updatedAt = now
                )
                subtaskDao.upsertSubtask(subtaskEntity)
            }
        }
    }

    override suspend fun deleteTask(taskId: String) {
        database.withTransaction {
            val now = clock.millis()
            taskDao.softDeleteTask(taskId, deletedAt = now, updatedAt = now)
            val subtasks = subtaskDao.getSubtasksByTaskId(taskId)
            for (sub in subtasks) {
                subtaskDao.softDeleteSubtask(sub.id, deletedAt = now, updatedAt = now)
            }
        }
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

            // Sinkronisasi otomatis status penyelesaian task induk (F5)
            recalculateParentTaskCompletion(subtask.taskId, now)
        }
    }

    override suspend fun upsertSubtask(subtask: Subtask) {
        database.withTransaction {
            val now = clock.millis()
            val targetSubtask = if (subtask.id.isBlank()) subtask.copy(id = idGenerator()) else subtask
            subtaskDao.upsertSubtask(targetSubtask.toEntity().copy(updatedAt = now))
            recalculateParentTaskCompletion(targetSubtask.taskId, now)
        }
    }

    override suspend fun deleteSubtask(subtaskId: String) {
        database.withTransaction {
            val subtask = subtaskDao.getSubtaskById(subtaskId) ?: return@withTransaction
            val now = clock.millis()
            subtaskDao.softDeleteSubtask(subtaskId, deletedAt = now, updatedAt = now)
            recalculateParentTaskCompletion(subtask.taskId, now)
        }
    }

    private suspend fun recalculateParentTaskCompletion(taskId: String, now: Long) {
        val total = subtaskDao.countTotalSubtasks(taskId)
        val open = subtaskDao.countOpenSubtasks(taskId)
        val isCompleted = total > 0 && open == 0
        taskDao.updateTaskCompletion(
            taskId = taskId,
            isCompleted = isCompleted,
            completedAt = if (isCompleted) now else null,
            updatedAt = now
        )
    }
}
