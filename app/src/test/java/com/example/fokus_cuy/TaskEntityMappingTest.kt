package com.example.fokus_cuy

import com.example.fokus_cuy.data.local.entity.DistractionNoteEntity
import com.example.fokus_cuy.data.local.entity.FocusSessionEntity
import com.example.fokus_cuy.data.local.entity.SubtaskEntity
import com.example.fokus_cuy.data.local.entity.TaskEntity
import com.example.fokus_cuy.data.local.entity.UserEntity
import com.example.fokus_cuy.data.local.entity.relation.TaskWithSubtasks
import com.example.fokus_cuy.data.local.mapper.toDomainModel
import com.example.fokus_cuy.data.local.mapper.toEntity
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskEntityMappingTest {

    @Test
    fun `toDomain and toEntity converts User properly`() {
        val user = User(
            id = "u1",
            name = "Dimas",
            email = "dimas@kampus.ac.id",
            campus = "Teknik Informatika",
            authProvider = AuthProvider.GOOGLE_CAMPUS,
            targetDailyMinutes = 120,
            soundFxEnabled = true,
            createdAt = 1000L,
            updatedAt = 2000L
        )

        val entity = user.toEntity()
        assertEquals("u1", entity.id)
        assertEquals("dimas@kampus.ac.id", entity.email)
        assertEquals("GOOGLE_CAMPUS", entity.authProvider)

        val backToDomain = entity.toDomainModel()
        assertEquals(user, backToDomain)
    }

    @Test
    fun `toDomain and toEntity converts Task and Subtasks properly`() {
        val task = Task(
            id = "t1",
            userId = "u1",
            title = "Laporan Basis Data",
            quadrant = Quadrant.Q1,
            scheduledDate = "2026-11-02",
            source = TaskSource.MANUAL,
            isCompleted = false,
            createdAt = 1000L,
            updatedAt = 2000L,
            subtasks = listOf(
                Subtask(id = "s1", taskId = "t1", title = "Langkah 1", orderIndex = 0, isCompleted = true),
                Subtask(id = "s2", taskId = "t1", title = "Langkah 2", orderIndex = 1, isCompleted = false)
            )
        )

        val taskEntity = task.toEntity()
        assertEquals("t1", taskEntity.id)
        assertEquals("Q1", taskEntity.quadrant)
        assertEquals("2026-11-02", taskEntity.scheduledDate)

        val subtaskEntities = task.subtasks.map { it.toEntity() }
        val relation = TaskWithSubtasks(task = taskEntity, subtasks = subtaskEntities)
        val domainFromRelation = relation.toDomainModel()

        assertEquals(task.id, domainFromRelation.id)
        assertEquals(task.title, domainFromRelation.title)
        assertEquals(2, domainFromRelation.subtasks.size)
        assertEquals("s1", domainFromRelation.subtasks[0].id)
        assertTrue(domainFromRelation.subtasks[0].isCompleted)
        assertEquals("s2", domainFromRelation.subtasks[1].id)
        assertFalse(domainFromRelation.subtasks[1].isCompleted)
    }

    @Test
    fun `toDomain and toEntity converts FocusSession properly`() {
        val session = FocusSession(
            id = "fs1",
            userId = "u1",
            taskId = "t1",
            mode = FocusMode.POMODORO,
            plannedDurationSec = 1500,
            actualDurationSec = 1500,
            status = FocusStatus.COMPLETED,
            startedAt = 1000L,
            endedAt = 2500L,
            createdAt = 1000L
        )

        val entity = session.toEntity()
        assertEquals("POMODORO", entity.mode)
        assertEquals("COMPLETED", entity.status)

        val backToDomain = entity.toDomainModel()
        assertEquals(session, backToDomain)
    }

    @Test
    fun `toDomain and toEntity converts DistractionNote properly`() {
        val note = DistractionNote(
            id = "dn1",
            userId = "u1",
            content = "Cek email kampus",
            isResolved = true,
            convertedTaskId = "t2",
            resolvedAt = 3000L,
            createdAt = 1000L,
            updatedAt = 3000L
        )

        val entity = note.toEntity()
        assertEquals("dn1", entity.id)
        assertTrue(entity.isResolved)
        assertEquals("t2", entity.convertedTaskId)

        val backToDomain = entity.toDomainModel()
        assertEquals(note, backToDomain)
    }
}
