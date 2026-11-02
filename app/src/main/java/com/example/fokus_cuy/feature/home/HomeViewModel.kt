package com.example.fokus_cuy.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.fokus_cuy.FokusCuyApplication
import com.example.fokus_cuy.domain.StreakCalculator
import com.example.fokus_cuy.domain.model.Quadrant
import com.example.fokus_cuy.domain.model.TaskSource
import com.example.fokus_cuy.domain.repository.FocusSessionRepository
import com.example.fokus_cuy.domain.repository.TaskRepository
import com.example.fokus_cuy.domain.repository.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
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
            days.add(
                WeekDay(
                    dayName = name,
                    dateNum = d.dayOfMonth.toString(),
                    dateString = d.toString(),
                    isToday = (d == today)
                )
            )
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
    private val userIdFlow = currentUserFlow.map { it?.id ?: DEFAULT_GUEST_ID }.distinctUntilChanged()

    private val tasksForSelectedDateFlow = combine(userIdFlow, selectedDayIndexFlow) { userId, dayIndex ->
        val dateString = weekDaysList.getOrNull(dayIndex)?.dateString ?: today.toString()
        userId to dateString
    }.distinctUntilChanged().flatMapLatest { (userId, dateString) ->
        taskRepository.observeTasksByDate(userId, dateString)
    }

    private val focusDaysFlow = userIdFlow.flatMapLatest { userId ->
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

        // Prioritas main task: Q1 belum selesai > Tugas apapun belum selesai > Tugas pertama
        val mainTask = tasks.firstOrNull { it.quadrant == Quadrant.Q1 && !it.isCompleted }
            ?: tasks.firstOrNull { !it.isCompleted }
            ?: tasks.firstOrNull()

        val hasMainTask = mainTask != null
        val nextIncompleteSubtask = mainTask?.subtasks?.firstOrNull { !it.isCompleted }
        val lastCompletedSubtask = mainTask?.subtasks?.lastOrNull { it.isCompleted }

        val mainTaskTitle = mainTask?.title ?: if (tasks.isEmpty()) "Belum ada tugas hari ini" else "Semua tugas tuntas!"
        val mainTaskNextStep = if (mainTask != null) {
            nextIncompleteSubtask?.title ?: if (mainTask.isCompleted) "Semua langkah selesai!" else "Langkah awal"
        } else {
            "Tekan + untuk membuat tugas baru"
        }

        val mainTaskCompletedSteps = mainTask?.subtasks?.count { it.isCompleted }
            ?: if (mainTask?.isCompleted == true) 1 else 0
        val mainTaskTotalSteps = if ((mainTask?.subtasks?.size ?: 0) > 0) mainTask!!.subtasks.size
            else if (hasMainTask) 1 else 0
        val mainTaskPomodoroCount = mainTask?.subtasks?.sumOf { it.pomodoroEstimate }?.takeIf { it > 0 }
            ?: if (hasMainTask) 1 else 0

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
            hasMainTask = hasMainTask,
            mainTaskId = mainTask?.id,
            mainTaskTitle = mainTaskTitle,
            mainTaskNextStep = mainTaskNextStep,
            mainTaskQuadrant = mainTask?.quadrant ?: Quadrant.Q1,
            mainTaskSource = mainTask?.source ?: TaskSource.MANUAL,
            mainTaskCompletedSteps = mainTaskCompletedSteps,
            mainTaskTotalSteps = mainTaskTotalSteps,
            mainTaskPomodoroCount = mainTaskPomodoroCount,
            isMainTaskChecked = mainTask?.isCompleted ?: false,
            mainTaskFirstIncompleteSubtaskId = nextIncompleteSubtask?.id,
            mainTaskLastCompletedSubtaskId = lastCompletedSubtask?.id,
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
            val subtaskIdToComplete = state.mainTaskFirstIncompleteSubtaskId
            val lastDoneSubtaskId = state.mainTaskLastCompletedSubtaskId

            if (subtaskIdToComplete != null) {
                // Centang langkah berikutnya
                taskRepository.toggleSubtask(subtaskIdToComplete, true)
            } else if (state.mainTaskTotalSteps > 0 && lastDoneSubtaskId != null) {
                // Semua langkah sudah selesai, batalkan centang langkah terakhir
                taskRepository.toggleSubtask(lastDoneSubtaskId, false)
            } else {
                // Tugas mandiri tanpa subtask
                taskRepository.toggleTaskCompletion(taskId, !state.isMainTaskChecked)
            }
        }
    }

    companion object {
        private const val DEFAULT_GUEST_ID = "user_guest_local"

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
