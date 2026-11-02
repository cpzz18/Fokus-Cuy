package com.example.fokus_cuy.domain.repository

import com.example.fokus_cuy.domain.model.DistractionNote
import com.example.fokus_cuy.domain.model.Quadrant
import kotlinx.coroutines.flow.Flow

interface DistractionRepository {
    fun observeActiveNotes(userId: String): Flow<List<DistractionNote>>
    suspend fun createNote(note: DistractionNote)
    suspend fun resolveNote(noteId: String)
    suspend fun convertToTask(noteId: String, quadrant: Quadrant, scheduledDate: String?): String
}
