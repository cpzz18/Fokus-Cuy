package com.example.fokus_cuy.domain.repository

import com.example.fokus_cuy.domain.model.FocusSession
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface FocusSessionRepository {
    suspend fun startSession(session: FocusSession)
    suspend fun updateSession(session: FocusSession)
    fun findRunningSession(userId: String): Flow<FocusSession?>
    fun observeFocusDays(userId: String): Flow<Set<LocalDate>>
    fun observeTodayFocusMinutes(userId: String, today: LocalDate): Flow<Int>
}
