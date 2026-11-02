package com.example.fokus_cuy.data.seed

import com.example.fokus_cuy.domain.model.FocusMode
import com.example.fokus_cuy.domain.model.FocusSession
import com.example.fokus_cuy.domain.model.FocusStatus
import com.example.fokus_cuy.domain.model.Quadrant
import com.example.fokus_cuy.domain.model.Subtask
import com.example.fokus_cuy.domain.model.Task
import com.example.fokus_cuy.domain.model.TaskSource
import com.example.fokus_cuy.domain.repository.FocusSessionRepository
import com.example.fokus_cuy.domain.repository.TaskRepository
import kotlinx.coroutines.flow.first
import java.time.Clock
import java.time.LocalDate

object DebugDatabaseSeeder {

    suspend fun seedIfEmpty(
        userId: String,
        taskRepository: TaskRepository,
        focusSessionRepository: FocusSessionRepository,
        clock: Clock = Clock.systemDefaultZone()
    ) {
        val today = LocalDate.now(clock)
        val todayStr = today.toString()

        if (taskRepository.hasAnyTask(userId)) {
            return
        }

        val now = clock.millis()
        val demoTaskId = "demo_task_lap_akhir"

        val demoTask = Task(
            id = demoTaskId,
            userId = userId,
            title = "Laporan Akhir Praktikum Basis Data",
            quadrant = Quadrant.Q1,
            scheduledDate = todayStr,
            source = TaskSource.MANUAL,
            isCompleted = false,
            createdAt = now,
            updatedAt = now,
            subtasks = listOf(
                Subtask(
                    id = "demo_sub_1",
                    taskId = demoTaskId,
                    title = "Desain Skema ERD & DDL Relasional",
                    orderIndex = 0,
                    pomodoroEstimate = 2,
                    isCompleted = true,
                    completedAt = now - 3600_000L,
                    createdAt = now - 3600_000L,
                    updatedAt = now - 3600_000L
                ),
                Subtask(
                    id = "demo_sub_2",
                    taskId = demoTaskId,
                    title = "Normalisasi 1NF sampai 3NF",
                    orderIndex = 1,
                    pomodoroEstimate = 1,
                    isCompleted = true,
                    completedAt = now - 1800_000L,
                    createdAt = now - 1800_000L,
                    updatedAt = now - 1800_000L
                ),
                Subtask(
                    id = "demo_sub_3",
                    taskId = demoTaskId,
                    title = "Implementasi Stored Procedure & Trigger",
                    orderIndex = 2,
                    pomodoroEstimate = 2,
                    isCompleted = false,
                    createdAt = now,
                    updatedAt = now
                ),
                Subtask(
                    id = "demo_sub_4",
                    taskId = demoTaskId,
                    title = "Benchmark Query Index vs Full Scan",
                    orderIndex = 3,
                    pomodoroEstimate = 1,
                    isCompleted = false,
                    createdAt = now,
                    updatedAt = now
                ),
                Subtask(
                    id = "demo_sub_5",
                    taskId = demoTaskId,
                    title = "Penyusunan Bab Pembahasan & Ekspor PDF",
                    orderIndex = 4,
                    pomodoroEstimate = 2,
                    isCompleted = false,
                    createdAt = now,
                    updatedAt = now
                )
            )
        )

        taskRepository.upsertTask(demoTask)

        // Seed 1 sesi fokus kemarin agar streak awal terhitung 1 hari
        val yesterdayMillis = now - 86400_000L
        focusSessionRepository.startSession(
            FocusSession(
                id = "demo_session_yesterday",
                userId = userId,
                taskId = demoTaskId,
                subtaskId = "demo_sub_1",
                mode = FocusMode.POMODORO,
                plannedDurationSec = 1500,
                actualDurationSec = 1500,
                status = FocusStatus.COMPLETED,
                startedAt = yesterdayMillis,
                endedAt = yesterdayMillis + 1500_000L,
                createdAt = yesterdayMillis
            )
        )
    }
}
