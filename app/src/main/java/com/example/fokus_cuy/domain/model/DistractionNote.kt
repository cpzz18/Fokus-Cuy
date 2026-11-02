package com.example.fokus_cuy.domain.model

data class DistractionNote(
    val id: String,
    val userId: String,
    val sessionId: String? = null,
    val content: String,
    val isResolved: Boolean = false,
    val convertedTaskId: String? = null,
    val resolvedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    init {
        require(id.isNotBlank()) { "Distraction note id cannot be blank" }
        require(userId.isNotBlank()) { "User id cannot be blank" }
        require(content.isNotBlank() && content.length <= 1000) {
            "Content must be 1..1000 characters"
        }
    }
}
