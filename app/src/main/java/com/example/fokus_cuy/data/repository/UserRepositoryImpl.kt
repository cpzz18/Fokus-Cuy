package com.example.fokus_cuy.data.repository

import com.example.fokus_cuy.data.local.AppDatabase
import com.example.fokus_cuy.data.local.mapper.toDomainModel
import com.example.fokus_cuy.data.local.mapper.toEntity
import com.example.fokus_cuy.domain.model.AuthProvider
import com.example.fokus_cuy.domain.model.User
import com.example.fokus_cuy.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Clock

class UserRepositoryImpl(
    database: AppDatabase,
    private val clock: Clock = Clock.systemDefaultZone()
) : UserRepository {

    private val userDao = database.userDao()

    override fun observeCurrentUser(): Flow<User?> {
        return userDao.observeCurrentUser().map { it?.toDomainModel() }
    }

    override suspend fun upsertUser(user: User) {
        userDao.upsertUser(user.toEntity())
    }

    override suspend fun ensureLocalGuestUser(): User {
        val current = userDao.getCurrentUser()
        if (current != null) {
            return current.toDomainModel()
        }

        val now = clock.millis()
        val defaultGuest = User(
            id = "user_guest_local",
            name = "Sobat Fokus",
            email = null,
            campus = null,
            avatarUrl = null,
            authProvider = AuthProvider.GUEST,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            lastLoginAt = now,
            createdAt = now,
            updatedAt = now,
            deletedAt = null
        )

        userDao.upsertUser(defaultGuest.toEntity())
        return defaultGuest
    }
}
