package com.example.fokus_cuy.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.fokus_cuy.data.local.entity.DistractionNoteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DistractionNoteDao {

    @Query(
        """
        SELECT * FROM distraction_notes 
        WHERE user_id = :userId 
          AND is_resolved = 0 
        ORDER BY created_at DESC
        """
    )
    fun observeActiveNotes(userId: String): Flow<List<DistractionNoteEntity>>

    @Query("SELECT * FROM distraction_notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: String): DistractionNoteEntity?

    @Upsert
    suspend fun upsertNote(note: DistractionNoteEntity)

    @Query(
        """
        UPDATE distraction_notes 
        SET is_resolved = 1, 
            converted_task_id = :convertedTaskId, 
            resolved_at = :resolvedAt, 
            updated_at = :updatedAt 
        WHERE id = :id
        """
    )
    suspend fun resolveNote(
        id: String,
        convertedTaskId: String?,
        resolvedAt: Long,
        updatedAt: Long
    )
}
