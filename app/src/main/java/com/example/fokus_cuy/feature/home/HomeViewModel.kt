package com.example.fokus_cuy.feature.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun selectDay(index: Int) {
        _uiState.update { it.copy(selectedDayIndex = index) }
    }

    fun nextQuote() {
        _uiState.update { state ->
            val nextIdx = if (state.quotes.isNotEmpty()) (state.quoteIndex + 1) % state.quotes.size else 0
            state.copy(quoteIndex = nextIdx)
        }
    }

    fun toggleMainTask() {
        _uiState.update { it.copy(isMainTaskChecked = !it.isMainTaskChecked) }
    }
}
