package com.example.fokus_cuy.domain.model

data class Subtask(
    val id: String,
    val taskId: String,
    val title: String,
    val orderIndex: Int = 0,
    val pomodoroEstimate: Int = 1,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null
) {
    init {
        require(id.isNotBlank()) { "Subtask id cannot be blank" }
        require(taskId.isNotBlank()) { "Task id cannot be blank" }
        require(title.isNotBlank() && title.length <= 255) { "Subtask title must be 1..255 characters" }
        require(orderIndex >= 0) { "Order index must be non-negative" }
        require(pomodoroEstimate in 1..4) { "Pomodoro estimate must be between 1 and 4" }
    }
}
