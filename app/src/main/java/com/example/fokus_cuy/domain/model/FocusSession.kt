package com.example.fokus_cuy.domain.model

data class FocusSession(
    val id: String,
    val userId: String,
    val taskId: String? = null,
    val subtaskId: String? = null,
    val mode: FocusMode = FocusMode.POMODORO,
    val plannedDurationSec: Int = 1500,
    val actualDurationSec: Int = 0,
    val status: FocusStatus = FocusStatus.RUNNING,
    val startedAt: Long = System.currentTimeMillis(),
    val endedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    init {
        require(id.isNotBlank()) { "Session id cannot be blank" }
        require(userId.isNotBlank()) { "User id cannot be blank" }
        require(plannedDurationSec > 0) { "Planned duration must be positive" }
        require(actualDurationSec >= 0) { "Actual duration must be non-negative" }
        endedAt?.let {
            require(it >= startedAt) { "endedAt ($it) must be >= startedAt ($startedAt)" }
        }
    }
}
