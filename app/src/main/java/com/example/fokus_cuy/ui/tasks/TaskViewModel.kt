package com.example.fokus_cuy.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.fokus_cuy.domain.model.TaskItem
import com.example.fokus_cuy.domain.repository.TaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TaskViewModel(
    private val repository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState(isLoading = true))
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
    }

    private fun loadTasks() {
        viewModelScope.launch {
            repository.getAllTasks()
                .catch { e ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
                }
                .collect { list ->
                    _uiState.update { it.copy(tasks = list, isLoading = false, errorMessage = null) }
                }
        }
    }

    fun onTitleChange(title: String) {
        _uiState.update { it.copy(inputTitle = title) }
    }

    fun onDescriptionChange(desc: String) {
        _uiState.update { it.copy(inputDescription = desc) }
    }

    fun addTask() {
        val title = _uiState.value.inputTitle.trim()
        if (title.isBlank()) return

        val desc = _uiState.value.inputDescription.trim()
        viewModelScope.launch {
            repository.insertTask(TaskItem(title = title, description = desc))
            _uiState.update { it.copy(inputTitle = "", inputDescription = "") }
        }
    }

    fun toggleTask(task: TaskItem) {
        viewModelScope.launch {
            repository.updateTask(task.copy(isCompleted = !task.isCompleted))
        }
    }

    fun deleteTask(task: TaskItem) {
        viewModelScope.launch {
            repository.deleteTask(task)
        }
    }

    companion object {
        fun provideFactory(repository: TaskRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return TaskViewModel(repository) as T
                }
            }
    }
}
