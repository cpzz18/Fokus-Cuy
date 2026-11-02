package com.example.fokus_cuy

import com.example.fokus_cuy.domain.model.FocusSession
import com.example.fokus_cuy.domain.model.Quadrant
import com.example.fokus_cuy.domain.model.Subtask
import com.example.fokus_cuy.domain.model.Task
import com.example.fokus_cuy.domain.model.TaskSource
import com.example.fokus_cuy.domain.model.User
import com.example.fokus_cuy.domain.repository.FocusSessionRepository
import com.example.fokus_cuy.domain.repository.TaskRepository
import com.example.fokus_cuy.domain.repository.UserRepository
import com.example.fokus_cuy.feature.home.HomeUiState
import com.example.fokus_cuy.feature.home.HomeViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fixedClock = Clock.fixed(Instant.parse("2026-11-02T08:00:00Z"), ZoneId.of("UTC")) // Monday
    private val today = LocalDate.of(2026, 11, 2)

    private lateinit var fakeTaskRepo: FakeTaskRepository
    private lateinit var fakeFocusRepo: FakeFocusSessionRepository
    private lateinit var fakeUserRepo: FakeUserRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeTaskRepo = FakeTaskRepository()
        fakeFocusRepo = FakeFocusSessionRepository()
        fakeUserRepo = FakeUserRepository(
            User(id = "user_1", name = "Dimas")
        )

        viewModel = HomeViewModel(
            taskRepository = fakeTaskRepo,
            focusSessionRepository = fakeFocusRepo,
            userRepository = fakeUserRepo,
            clock = fixedClock
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState has expected values and reads user profile`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()
        val state = viewModel.uiState.value

        assertEquals("Dimas", state.userName)
        assertEquals(7, state.weekDays.size)
        assertEquals(0, state.quoteIndex)
        assertTrue(state.currentQuote.isNotBlank())
        assertTrue(state.weekDays.first().isToday) // 2026-11-02 is Monday
    }

    @Test
    fun `selectDay updates selectedDayIndex properly`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()
        viewModel.selectDay(2)
        advanceUntilIdle()

        assertEquals(2, viewModel.uiState.value.selectedDayIndex)
    }

    @Test
    fun `nextQuote advances index and wraps around properly`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        advanceUntilIdle()
        val quotesCount = viewModel.uiState.value.quotes.size
        assertTrue(quotesCount > 0)

        viewModel.nextQuote()
        advanceUntilIdle()
        assertEquals(1, viewModel.uiState.value.quoteIndex)

        repeat(quotesCount - 1) {
            viewModel.nextQuote()
        }
        advanceUntilIdle()
        assertEquals(0, viewModel.uiState.value.quoteIndex)
    }

    @Test
    fun `task with subtasks updates progress and main task metadata on toggle`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        val task = Task(
            id = "t1",
            userId = "user_1",
            title = "Tugas Penting",
            quadrant = Quadrant.Q1,
            source = TaskSource.AI_PDF,
            scheduledDate = today.toString(),
            subtasks = listOf(
                Subtask(id = "s1", taskId = "t1", title = "Step 1", pomodoroEstimate = 2, isCompleted = false),
                Subtask(id = "s2", taskId = "t1", title = "Step 2", pomodoroEstimate = 3, isCompleted = false)
            )
        )
        fakeTaskRepo.upsertTask(task)
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertTrue(state.hasMainTask)
        assertEquals("Tugas Penting", state.mainTaskTitle)
        assertEquals("Step 1", state.mainTaskNextStep)
        assertEquals(0f, state.progress)
        assertEquals(0, state.mainTaskCompletedSteps)
        assertEquals(2, state.mainTaskTotalSteps)
        assertEquals(5, state.mainTaskPomodoroCount) // 2 + 3
        assertEquals(TaskSource.AI_PDF, state.mainTaskSource)
        assertEquals(Quadrant.Q1, state.mainTaskQuadrant)
        assertFalse(state.isMainTaskChecked)

        // Toggle first step
        viewModel.toggleMainTask()
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(0.5f, state.progress)
        assertEquals(1, state.mainTaskCompletedSteps)
        assertEquals("Step 2", state.mainTaskNextStep)

        // Toggle second step
        viewModel.toggleMainTask()
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(1.0f, state.progress)
        assertEquals(2, state.mainTaskCompletedSteps)
        assertTrue(state.isMainTaskChecked)
        assertEquals("Semua langkah selesai!", state.mainTaskNextStep)

        // Toggle again when fully completed -> unchecks last step
        viewModel.toggleMainTask()
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertEquals(0.5f, state.progress)
        assertFalse(state.isMainTaskChecked)
    }

    @Test
    fun `standalone task without subtasks toggles completion properly`() = runTest {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.uiState.collect()
        }
        val standalone = Task(
            id = "t_simple",
            userId = "user_1",
            title = "Baca 1 Bab",
            scheduledDate = today.toString(),
            subtasks = emptyList()
        )
        fakeTaskRepo.upsertTask(standalone)
        advanceUntilIdle()

        var state = viewModel.uiState.value
        assertTrue(state.hasMainTask)
        assertEquals("Baca 1 Bab", state.mainTaskTitle)
        assertFalse(state.isMainTaskChecked)

        // Toggle to completed
        viewModel.toggleMainTask()
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertTrue(state.isMainTaskChecked)

        // Toggle back to incomplete
        viewModel.toggleMainTask()
        advanceUntilIdle()

        state = viewModel.uiState.value
        assertFalse(state.isMainTaskChecked)
    }

    @Test
    fun `greetingTitle adapts based on local time of day`() {
        val morningState = HomeUiState(userName = "Dimas", currentHour = 8)
        assertEquals("Pagi, Dimas! Siap bikin hari ini produktif?", morningState.greetingTitle)

        val afternoonState = HomeUiState(userName = "Dimas", currentHour = 14)
        assertEquals("Halo, Dimas! Yuk, cicil dikit lagi sebelum santai.", afternoonState.greetingTitle)

        val nightState = HomeUiState(userName = "Dimas", currentHour = 21)
        assertEquals("Malam, Dimas! Selesaikan 1 langkah kecil, habis itu istirahat ya.", nightState.greetingTitle)
    }

    @Test
    fun `progressMotivationText adapts based on progress percentage`() {
        val zeroState = HomeUiState(progress = 0f)
        assertEquals("Mesin masih dingin nih. Yuk, centang langkah pertamamu!", zeroState.progressMotivationText)

        val inProgressState = HomeUiState(progress = 0.38f)
        assertEquals("Mantap! Udah 38% jalan menuju bebas tugas hari ini.", inProgressState.progressMotivationText)

        val completedState = HomeUiState(progress = 1.0f)
        assertEquals("Gokil! Semua target hari ini ludes. Waktunya menikmati hari!", completedState.progressMotivationText)
    }

    private class FakeTaskRepository : TaskRepository {
        private val tasksFlow = MutableStateFlow<Map<String, Task>>(emptyMap())

        override fun observeTasksByDate(userId: String, date: String): Flow<List<Task>> {
            return tasksFlow.map { map ->
                map.values.filter { it.userId == userId && it.scheduledDate == date && it.deletedAt == null }
            }
        }

        override fun observeTasksByQuadrant(userId: String, quadrant: Quadrant): Flow<List<Task>> {
            return tasksFlow.map { map ->
                map.values.filter { it.userId == userId && it.quadrant == quadrant && it.deletedAt == null }
            }
        }

        override fun getTaskById(taskId: String): Flow<Task?> {
            return tasksFlow.map { it[taskId] }
        }

        override suspend fun hasAnyTask(userId: String): Boolean {
            return tasksFlow.value.values.any { it.userId == userId && it.deletedAt == null }
        }

        override suspend fun upsertTask(task: Task) {
            val current = tasksFlow.value.toMutableMap()
            current[task.id] = task
            tasksFlow.value = current
        }

        override suspend fun deleteTask(taskId: String) {
            val current = tasksFlow.value.toMutableMap()
            val existing = current[taskId]
            if (existing != null) {
                current[taskId] = existing.copy(deletedAt = System.currentTimeMillis())
                tasksFlow.value = current
            }
        }

        override suspend fun toggleTaskCompletion(taskId: String, isCompleted: Boolean) {
            val current = tasksFlow.value.toMutableMap()
            val existing = current[taskId]
            if (existing != null) {
                current[taskId] = existing.copy(isCompleted = isCompleted)
                tasksFlow.value = current
            }
        }

        override suspend fun toggleSubtask(subtaskId: String, isCompleted: Boolean) {
            val current = tasksFlow.value.toMutableMap()
            for ((taskId, task) in current) {
                val subIdx = task.subtasks.indexOfFirst { it.id == subtaskId }
                if (subIdx >= 0) {
                    val updatedSubtasks = task.subtasks.toMutableList()
                    val targetSub = updatedSubtasks[subIdx]
                    updatedSubtasks[subIdx] = targetSub.copy(isCompleted = isCompleted)
                    val allDone = updatedSubtasks.isNotEmpty() && updatedSubtasks.all { it.isCompleted }
                    current[taskId] = task.copy(
                        subtasks = updatedSubtasks,
                        isCompleted = allDone
                    )
                    tasksFlow.value = current
                    break
                }
            }
        }

        override suspend fun upsertSubtask(subtask: Subtask) {}
        override suspend fun deleteSubtask(subtaskId: String) {}
    }

    private class FakeFocusSessionRepository : FocusSessionRepository {
        private val focusDaysFlow = MutableStateFlow<Set<LocalDate>>(emptySet())

        override suspend fun startSession(session: FocusSession) {}
        override suspend fun updateSession(session: FocusSession) {}
        override fun findRunningSession(userId: String): Flow<FocusSession?> = MutableStateFlow(null)
        override fun observeFocusDays(userId: String): Flow<Set<LocalDate>> = focusDaysFlow
        override fun observeTodayFocusMinutes(userId: String, today: LocalDate): Flow<Int> = MutableStateFlow(0)
    }

    private class FakeUserRepository(initialUser: User? = null) : UserRepository {
        private val userFlow = MutableStateFlow(initialUser)

        override fun observeCurrentUser(): Flow<User?> = userFlow
        override suspend fun upsertUser(user: User) {
            userFlow.value = user
        }
        override suspend fun ensureLocalGuestUser(): User {
            return userFlow.value ?: User(id = "user_1", name = "Dimas").also { userFlow.value = it }
        }
    }
}
