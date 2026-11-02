package com.example.fokus_cuy.domain.model

data class User(
    val id: String,
    val name: String,
    val email: String? = null,
    val campus: String? = null,
    val avatarUrl: String? = null,
    val authProvider: AuthProvider = AuthProvider.GUEST,
    val targetDailyMinutes: Int = 100,
    val soundFxEnabled: Boolean = true,
    val lastLoginAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
) {
    init {
        require(id.isNotBlank()) { "User id cannot be blank" }
        require(name.isNotBlank() && name.length <= 100) { "User name must be 1..100 characters" }
        require(targetDailyMinutes in 1..1440) { "Target daily minutes must be between 1 and 1440" }
    }
}
