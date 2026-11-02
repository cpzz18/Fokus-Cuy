package com.example.fokus_cuy.data.repository

import androidx.room.withTransaction
import com.example.fokus_cuy.data.local.AppDatabase
import com.example.fokus_cuy.data.local.entity.TaskEntity
import com.example.fokus_cuy.data.local.mapper.toDomainModel
import com.example.fokus_cuy.data.local.mapper.toEntity
import com.example.fokus_cuy.domain.model.DistractionNote
import com.example.fokus_cuy.domain.model.Quadrant
import com.example.fokus_cuy.domain.model.TaskSource
import com.example.fokus_cuy.domain.repository.DistractionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.util.UUID

class DistractionRepositoryImpl(
    private val database: AppDatabase,
    private val clock: Clock = Clock.systemDefaultZone(),
    private val idGenerator: () -> String = { UUID.randomUUID().toString() }
) : DistractionRepository {

    private val distractionDao = database.distractionNoteDao()
    private val taskDao = database.taskDao()

    override fun observeActiveNotes(userId: String): Flow<List<DistractionNote>> {
        return distractionDao.observeActiveNotes(userId).map { list ->
            list.map { it.toDomainModel() }
        }
    }

    override suspend fun createNote(note: DistractionNote) {
        val target = if (note.id.isBlank()) note.copy(id = idGenerator()) else note
        distractionDao.upsertNote(target.toEntity())
    }

    override suspend fun resolveNote(noteId: String) {
        val now = clock.millis()
        distractionDao.resolveNote(
            id = noteId,
            convertedTaskId = null,
            resolvedAt = now,
            updatedAt = now
        )
    }

    override suspend fun convertToTask(
        noteId: String,
        quadrant: Quadrant,
        scheduledDate: String?
    ): String {
        return database.withTransaction {
            val note = distractionDao.getNoteById(noteId)
                ?: throw IllegalArgumentException("Distraction note $noteId not found")

            val now = clock.millis()
            val newTaskId = idGenerator()
            val newTaskEntity = TaskEntity(
                id = newTaskId,
                userId = note.userId,
                title = note.content,
                quadrant = quadrant.name,
                scheduledDate = scheduledDate,
                source = TaskSource.MANUAL.name,
                sourceFileName = null,
                isCompleted = false,
                completedAt = null,
                createdAt = now,
                updatedAt = now,
                deletedAt = null
            )

            taskDao.upsertTask(newTaskEntity)
            distractionDao.resolveNote(
                id = noteId,
                convertedTaskId = newTaskId,
                resolvedAt = now,
                updatedAt = now
            )

            newTaskId
        }
    }
}
