package com.example.fokus_cuy.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.fokus_cuy.data.local.entity.FocusSessionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FocusSessionDao {

    @Upsert
    suspend fun upsertSession(session: FocusSessionEntity)

    @Query(
        """
        SELECT * FROM focus_sessions 
        WHERE user_id = :userId AND status = 'RUNNING' 
        ORDER BY started_at DESC 
        LIMIT 1
        """
    )
    fun findRunningSession(userId: String): Flow<FocusSessionEntity?>

    @Query(
        """
        SELECT DISTINCT date(started_at / 1000, 'unixepoch', 'localtime') 
        FROM focus_sessions 
        WHERE user_id = :userId 
          AND status = 'COMPLETED' 
          AND mode != 'BREAK'
        """
    )
    fun observeCompletedFocusDates(userId: String): Flow<List<String>>

    @Query(
        """
        SELECT COALESCE(SUM(actual_duration_sec), 0) / 60 
        FROM focus_sessions 
        WHERE user_id = :userId 
          AND status = 'COMPLETED' 
          AND mode != 'BREAK' 
          AND date(started_at / 1000, 'unixepoch', 'localtime') = :dateString
        """
    )
    fun observeFocusMinutesForDate(userId: String, dateString: String): Flow<Int>
}
