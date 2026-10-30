package com.example.fokus_cuy.ui.tasks

import com.example.fokus_cuy.domain.model.TaskItem

data class TaskUiState(
    val tasks: List<TaskItem> = emptyList(),
    val inputTitle: String = "",
    val inputDescription: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
