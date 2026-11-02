package com.example.fokus_cuy.data.repository

import com.example.fokus_cuy.data.local.AppDatabase
import com.example.fokus_cuy.data.local.mapper.toDomainModel
import com.example.fokus_cuy.data.local.mapper.toEntity
import com.example.fokus_cuy.domain.model.FocusSession
import com.example.fokus_cuy.domain.repository.FocusSessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Clock
import java.time.LocalDate
import java.util.UUID

class FocusSessionRepositoryImpl(
    database: AppDatabase,
    private val clock: Clock = Clock.systemDefaultZone(),
    private val idGenerator: () -> String = { UUID.randomUUID().toString() }
) : FocusSessionRepository {

    private val focusSessionDao = database.focusSessionDao()

    override suspend fun startSession(session: FocusSession) {
        val target = if (session.id.isBlank()) session.copy(id = idGenerator()) else session
        focusSessionDao.upsertSession(target.toEntity())
    }

    override suspend fun updateSession(session: FocusSession) {
        focusSessionDao.upsertSession(session.toEntity())
    }

    override fun findRunningSession(userId: String): Flow<FocusSession?> {
        return focusSessionDao.findRunningSession(userId).map { it?.toDomainModel() }
    }

    override fun observeFocusDays(userId: String): Flow<Set<LocalDate>> {
        return focusSessionDao.observeCompletedFocusDates(userId).map { dateStrings ->
            dateStrings.mapNotNull {
                runCatching { LocalDate.parse(it) }.getOrNull()
            }.toSet()
        }
    }

    override fun observeTodayFocusMinutes(userId: String, today: LocalDate): Flow<Int> {
        val zone = clock.zone
        val startOfDayMillis = today.atStartOfDay(zone).toInstant().toEpochMilli()
        val endOfDayMillis = today.plusDays(1).atStartOfDay(zone).toInstant().toEpochMilli()
        return focusSessionDao.observeFocusMinutesForDateRange(userId, startOfDayMillis, endOfDayMillis)
    }
}
