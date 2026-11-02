package com.example.fokus_cuy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fokus_cuy.FokusCuyApplication
import com.example.fokus_cuy.domain.StreakCalculator
import com.example.fokus_cuy.domain.model.Quadrant
import com.example.fokus_cuy.domain.repository.FocusSessionRepository
import com.example.fokus_cuy.domain.repository.TaskRepository
import com.example.fokus_cuy.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val taskRepository: TaskRepository,
    private val focusSessionRepository: FocusSessionRepository,
    private val userRepository: UserRepository,
    private val clock: Clock = Clock.systemDefaultZone()
) : ViewModel() {

    private val today: LocalDate get() = LocalDate.now(clock)
    private val mondayOfWeek: LocalDate get() = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))

    private val weekDaysList: List<WeekDay> by lazy {
        val days = mutableListOf<WeekDay>()
        for (i in 0..6) {
            val d = mondayOfWeek.plusDays(i.toLong())
            val name = when (d.dayOfWeek) {
                DayOfWeek.MONDAY -> "Sen"
                DayOfWeek.TUESDAY -> "Sel"
                DayOfWeek.WEDNESDAY -> "Rab"
                DayOfWeek.THURSDAY -> "Kam"
                DayOfWeek.FRIDAY -> "Jum"
                DayOfWeek.SATURDAY -> "Sab"
                DayOfWeek.SUNDAY -> "Min"
                else -> ""
            }
            days.add(WeekDay(dayName = name, dateNum = d.dayOfMonth.toString(), dateString = d.toString()))
        }
        days
    }

    private val defaultDayIndex: Int by lazy {
        val todayStr = today.toString()
        val found = weekDaysList.indexOfFirst { it.dateString == todayStr }
        if (found >= 0) found else 0
    }

    private val selectedDayIndexFlow = MutableStateFlow(defaultDayIndex)
    private val quoteIndexFlow = MutableStateFlow(0)

    private val currentUserFlow = userRepository.observeCurrentUser()

    private val tasksForSelectedDateFlow = combine(currentUserFlow, selectedDayIndexFlow) { user, dayIndex ->
        val userId = user?.id ?: "user_guest_local"
        val dateString = weekDaysList.getOrNull(dayIndex)?.dateString ?: today.toString()
        userId to dateString
    }.flatMapLatest { (userId, dateString) ->
        taskRepository.observeTasksByDate(userId, dateString)
    }

    private val focusDaysFlow = currentUserFlow.flatMapLatest { user ->
        val userId = user?.id ?: "user_guest_local"
        focusSessionRepository.observeFocusDays(userId)
    }

    val uiState: StateFlow<HomeUiState> = combine(
        currentUserFlow,
        selectedDayIndexFlow,
        tasksForSelectedDateFlow,
        focusDaysFlow,
        quoteIndexFlow
    ) { user, dayIdx, tasks, focusDays, quoteIdx ->
        val userName = user?.name ?: "Sobat Fokus"
        val streak = StreakCalculator.calculate(focusDays, today)
        val selectedDateStr = weekDaysList.getOrNull(dayIdx)?.dateString ?: today.toString()

        var totalSteps = 0
        var completedSteps = 0

        for (t in tasks) {
            if (t.subtasks.isNotEmpty()) {
                totalSteps += t.subtasks.size
                completedSteps += t.subtasks.count { it.isCompleted }
            } else {
                totalSteps += 1
                if (t.isCompleted) completedSteps += 1
            }
        }

        val progress = if (totalSteps > 0) {
            (completedSteps.toFloat() / totalSteps.toFloat()).coerceIn(0f, 1f)
        } else {
            0f
        }

        val mainTask = tasks.firstOrNull { it.quadrant == Quadrant.Q1 && !it.isCompleted }
            ?: tasks.firstOrNull { !it.isCompleted }
            ?: tasks.firstOrNull()

        val nextIncompleteSubtask = mainTask?.subtasks?.firstOrNull { !it.isCompleted }
        val mainTaskTitle = mainTask?.title ?: if (tasks.isEmpty()) "Belum ada tugas hari ini" else "Semua tugas tuntas!"
        val mainTaskNextStep = if (mainTask != null) {
            nextIncompleteSubtask?.title ?: if (mainTask.isCompleted) "Semua langkah selesai!" else "Langkah awal"
        } else {
            "Tekan + untuk membuat tugas baru"
        }

        HomeUiState(
            userName = userName,
            streakDays = streak,
            weekDays = weekDaysList,
            selectedDayIndex = dayIdx,
            selectedDate = selectedDateStr,
            quoteIndex = quoteIdx,
            progress = progress,
            completedSteps = completedSteps,
            totalSteps = totalSteps,
            mainTaskId = mainTask?.id,
            mainTaskTitle = mainTaskTitle,
            mainTaskNextStep = mainTaskNextStep,
            isMainTaskChecked = mainTask?.isCompleted ?: false,
            mainTaskFirstIncompleteSubtaskId = nextIncompleteSubtask?.id,
            currentHour = LocalTime.now(clock).hour
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HomeUiState(
            weekDays = weekDaysList,
            selectedDayIndex = defaultDayIndex,
            selectedDate = weekDaysList.getOrNull(defaultDayIndex)?.dateString ?: today.toString(),
            currentHour = LocalTime.now(clock).hour
        )
    )

    fun selectDay(index: Int) {
        if (index in weekDaysList.indices) {
            selectedDayIndexFlow.value = index
        }
    }

    fun nextQuote() {
        val count = uiState.value.quotes.size
        quoteIndexFlow.update { if (count > 0) (it + 1) % count else 0 }
    }

    fun toggleMainTask() {
        viewModelScope.launch {
            val state = uiState.value
            val taskId = state.mainTaskId ?: return@launch
            val subtaskId = state.mainTaskFirstIncompleteSubtaskId

            if (subtaskId != null) {
                taskRepository.toggleSubtask(subtaskId, true)
            } else {
                val task = taskRepository.getTaskById(taskId).first()
                if (task != null && task.subtasks.isNotEmpty()) {
                    val lastDone = task.subtasks.lastOrNull { it.isCompleted }
                    if (lastDone != null) {
                        taskRepository.toggleSubtask(lastDone.id, false)
                    }
                }
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FokusCuyApplication
                val container = app.container
                HomeViewModel(
                    taskRepository = container.taskRepository,
                    focusSessionRepository = container.focusSessionRepository,
                    userRepository = container.userRepository,
                    clock = container.clock
                )
            }
        }
    }
}
