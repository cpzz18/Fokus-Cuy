package com.example.fokus_cuy.domain.repository

import com.example.fokus_cuy.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun observeCurrentUser(): Flow<User?>
    suspend fun upsertUser(user: User)
    suspend fun ensureLocalGuestUser(): User
}
