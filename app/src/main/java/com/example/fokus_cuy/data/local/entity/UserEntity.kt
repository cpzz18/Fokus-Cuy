package com.example.fokus_cuy.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val email: String? = null,
    val campus: String? = null,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String? = null,
    @ColumnInfo(name = "auth_provider")
    val authProvider: String,
    @ColumnInfo(name = "target_daily_minutes", defaultValue = "100")
    val targetDailyMinutes: Int,
    @ColumnInfo(name = "sound_fx_enabled", defaultValue = "1")
    val soundFxEnabled: Boolean,
    @ColumnInfo(name = "last_login_at")
    val lastLoginAt: Long? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long,
    @ColumnInfo(name = "deleted_at")
    val deletedAt: Long? = null
)
