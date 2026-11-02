package com.example.fokus_cuy.domain.model

data class Task(
    val id: String,
    val userId: String,
    val title: String,
    val quadrant: Quadrant = Quadrant.Q1,
    val scheduledDate: String? = null,
    val source: TaskSource = TaskSource.MANUAL,
    val sourceFileName: String? = null,
    val isCompleted: Boolean = false,
    val completedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = null,
    val subtasks: List<Subtask> = emptyList()
) {
    init {
        require(id.isNotBlank()) { "Task id cannot be blank" }
        require(userId.isNotBlank()) { "User id cannot be blank" }
        require(title.isNotBlank() && title.length <= 255) { "Task title must be 1..255 characters" }
        scheduledDate?.let {
            require(it.matches(Regex("""^\d{4}-\d{2}-\d{2}$"""))) {
                "scheduledDate must follow yyyy-MM-dd format, was '$it'"
            }
        }
    }
}
