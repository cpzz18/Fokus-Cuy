package com.example.fokus_cuy.data.local.mapper

import com.example.fokus_cuy.data.local.entity.DistractionNoteEntity
import com.example.fokus_cuy.data.local.entity.FocusSessionEntity
import com.example.fokus_cuy.data.local.entity.SubtaskEntity
import com.example.fokus_cuy.data.local.entity.TaskEntity
import com.example.fokus_cuy.data.local.entity.UserEntity
import com.example.fokus_cuy.data.local.entity.relation.TaskWithSubtasks
import com.example.fokus_cuy.domain.model.AuthProvider
import com.example.fokus_cuy.domain.model.DistractionNote
import com.example.fokus_cuy.domain.model.FocusMode
import com.example.fokus_cuy.domain.model.FocusSession
import com.example.fokus_cuy.domain.model.FocusStatus
import com.example.fokus_cuy.domain.model.Quadrant
import com.example.fokus_cuy.domain.model.Subtask
import com.example.fokus_cuy.domain.model.Task
import com.example.fokus_cuy.domain.model.TaskSource
import com.example.fokus_cuy.domain.model.User

fun UserEntity.toDomainModel(): User = User(
    id = id,
    name = name,
    email = email,
    campus = campus,
    avatarUrl = avatarUrl,
    authProvider = runCatching { AuthProvider.valueOf(authProvider) }.getOrDefault(AuthProvider.GUEST),
    targetDailyMinutes = targetDailyMinutes,
    soundFxEnabled = soundFxEnabled,
    lastLoginAt = lastLoginAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)

fun User.toEntity(): UserEntity = UserEntity(
    id = id,
    name = name,
    email = email,
    campus = campus,
    avatarUrl = avatarUrl,
    authProvider = authProvider.name,
    targetDailyMinutes = targetDailyMinutes,
    soundFxEnabled = soundFxEnabled,
    lastLoginAt = lastLoginAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)

fun SubtaskEntity.toDomainModel(): Subtask = Subtask(
    id = id,
    taskId = taskId,
    title = title,
    orderIndex = orderIndex,
    pomodoroEstimate = pomodoroEstimate,
    isCompleted = isCompleted,
    completedAt = completedAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)

fun Subtask.toEntity(): SubtaskEntity = SubtaskEntity(
    id = id,
    taskId = taskId,
    title = title,
    orderIndex = orderIndex,
    pomodoroEstimate = pomodoroEstimate,
    isCompleted = isCompleted,
    completedAt = completedAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)

fun TaskWithSubtasks.toDomainModel(): Task = Task(
    id = task.id,
    userId = task.userId,
    title = task.title,
    quadrant = runCatching { Quadrant.valueOf(task.quadrant) }.getOrDefault(Quadrant.Q1),
    scheduledDate = task.scheduledDate,
    source = runCatching { TaskSource.valueOf(task.source) }.getOrDefault(TaskSource.MANUAL),
    sourceFileName = task.sourceFileName,
    isCompleted = task.isCompleted,
    completedAt = task.completedAt,
    createdAt = task.createdAt,
    updatedAt = task.updatedAt,
    deletedAt = task.deletedAt,
    subtasks = subtasks
        .filter { it.deletedAt == null }
        .sortedBy { it.orderIndex }
        .map { it.toDomainModel() }
)

fun Task.toEntity(): TaskEntity = TaskEntity(
    id = id,
    userId = userId,
    title = title,
    quadrant = quadrant.name,
    scheduledDate = scheduledDate,
    source = source.name,
    sourceFileName = sourceFileName,
    isCompleted = isCompleted,
    completedAt = completedAt,
    createdAt = createdAt,
    updatedAt = updatedAt,
    deletedAt = deletedAt
)

fun FocusSessionEntity.toDomainModel(): FocusSession = FocusSession(
    id = id,
    userId = userId,
    taskId = taskId,
    subtaskId = subtaskId,
    mode = runCatching { FocusMode.valueOf(mode) }.getOrDefault(FocusMode.POMODORO),
    plannedDurationSec = plannedDurationSec,
    actualDurationSec = actualDurationSec,
    status = runCatching { FocusStatus.valueOf(status) }.getOrDefault(FocusStatus.RUNNING),
    startedAt = startedAt,
    endedAt = endedAt,
    createdAt = createdAt
)

fun FocusSession.toEntity(): FocusSessionEntity = FocusSessionEntity(
    id = id,
    userId = userId,
    taskId = taskId,
    subtaskId = subtaskId,
    mode = mode.name,
    plannedDurationSec = plannedDurationSec,
    actualDurationSec = actualDurationSec,
    status = status.name,
    startedAt = startedAt,
    endedAt = endedAt,
    createdAt = createdAt
)

fun DistractionNoteEntity.toDomainModel(): DistractionNote = DistractionNote(
    id = id,
    userId = userId,
    sessionId = sessionId,
    content = content,
    isResolved = isResolved,
    convertedTaskId = convertedTaskId,
    resolvedAt = resolvedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun DistractionNote.toEntity(): DistractionNoteEntity = DistractionNoteEntity(
    id = id,
    userId = userId,
    sessionId = sessionId,
    content = content,
    isResolved = isResolved,
    convertedTaskId = convertedTaskId,
    resolvedAt = resolvedAt,
    createdAt = createdAt,
    updatedAt = updatedAt
)
