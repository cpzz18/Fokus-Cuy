package com.example.fokus_cuy.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.fokus_cuy.data.local.entity.DistractionNoteEntity
import com.example.fokus_cuy.data.local.entity.FocusSessionEntity
import com.example.fokus_cuy.data.local.entity.SubtaskEntity
import com.example.fokus_cuy.data.local.entity.TaskEntity
import com.example.fokus_cuy.data.local.entity.UserEntity
import com.example.fokus_cuy.data.repository.DistractionRepositoryImpl
import com.example.fokus_cuy.data.repository.FocusSessionRepositoryImpl
import com.example.fokus_cuy.data.repository.TaskRepositoryImpl
import com.example.fokus_cuy.domain.model.AuthProvider
import com.example.fokus_cuy.domain.model.FocusMode
import com.example.fokus_cuy.domain.model.FocusSession
import com.example.fokus_cuy.domain.model.FocusStatus
import com.example.fokus_cuy.domain.model.Quadrant
import com.example.fokus_cuy.domain.model.Subtask
import com.example.fokus_cuy.domain.model.Task
import com.example.fokus_cuy.domain.model.TaskSource
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class RoomDatabaseIntegrationTest {

    private lateinit var db: AppDatabase
    private lateinit var taskRepository: TaskRepositoryImpl
    private lateinit var focusSessionRepository: FocusSessionRepositoryImpl
    private lateinit var distractionRepository: DistractionRepositoryImpl
    private val fixedClock = Clock.fixed(Instant.parse("2026-11-02T10:00:00Z"), ZoneId.of("UTC"))

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        taskRepository = TaskRepositoryImpl(db, fixedClock)
        focusSessionRepository = FocusSessionRepositoryImpl(db, fixedClock)
        distractionRepository = DistractionRepositoryImpl(db, fixedClock)
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun testTwoGuestUsersWithoutEmailCanCoexist() = runBlocking {
        val user1 = UserEntity(
            id = "guest_1",
            name = "Guest 1",
            email = null,
            authProvider = AuthProvider.GUEST.name,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        val user2 = UserEntity(
            id = "guest_2",
            name = "Guest 2",
            email = null,
            authProvider = AuthProvider.GUEST.name,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            createdAt = 2000L,
            updatedAt = 2000L
        )

        db.userDao().upsertUser(user1)
        db.userDao().upsertUser(user2)

        val currentUser = db.userDao().getCurrentUser()
        assertNotNull(currentUser)
        assertEquals("guest_1", currentUser?.id)
    }

    @Test
    fun testTaskSoftDeleteExcludedFromDateQuery() = runBlocking {
        val user = UserEntity(
            id = "u1",
            name = "User 1",
            authProvider = AuthProvider.GUEST.name,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        db.userDao().upsertUser(user)

        val task = Task(
            id = "t1",
            userId = "u1",
            title = "Task Active",
            scheduledDate = "2026-11-02",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        taskRepository.upsertTask(task)

        val initialList = taskRepository.observeTasksByDate("u1", "2026-11-02").first()
        assertEquals(1, initialList.size)

        taskRepository.deleteTask("t1")

        val afterDeleteList = taskRepository.observeTasksByDate("u1", "2026-11-02").first()
        assertEquals(0, afterDeleteList.size)
    }

    @Test
    fun testDeleteTaskSoftDeletesAllSubtasksInOneGo() = runBlocking {
        val user = UserEntity(
            id = "u1",
            name = "User 1",
            authProvider = AuthProvider.GUEST.name,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        db.userDao().upsertUser(user)

        val task = Task(
            id = "t_bulk",
            userId = "u1",
            title = "Bulk Task",
            scheduledDate = "2026-11-02",
            subtasks = listOf(
                Subtask(id = "sub_1", taskId = "t_bulk", title = "Step 1"),
                Subtask(id = "sub_2", taskId = "t_bulk", title = "Step 2")
            )
        )
        taskRepository.upsertTask(task)

        val subsBefore = db.subtaskDao().getSubtasksByTaskId("t_bulk")
        assertEquals(2, subsBefore.size)

        taskRepository.deleteTask("t_bulk")

        val subsAfter = db.subtaskDao().getSubtasksByTaskId("t_bulk")
        assertEquals(0, subsAfter.size)
    }

    @Test
    fun testToggleTaskWithoutSubtask() = runBlocking {
        val user = UserEntity(
            id = "u1",
            name = "User 1",
            authProvider = AuthProvider.GUEST.name,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        db.userDao().upsertUser(user)

        val standaloneTask = Task(
            id = "t_standalone",
            userId = "u1",
            title = "Belanja Buku",
            isCompleted = false
        )
        taskRepository.upsertTask(standaloneTask)

        taskRepository.toggleTaskCompletion("t_standalone", true)
        val completedTask = taskRepository.getTaskById("t_standalone").first()
        assertNotNull(completedTask)
        assertTrue(completedTask!!.isCompleted)
        assertNotNull(completedTask.completedAt)

        taskRepository.toggleTaskCompletion("t_standalone", false)
        val reopenedTask = taskRepository.getTaskById("t_standalone").first()
        assertNotNull(reopenedTask)
        assertFalse(reopenedTask!!.isCompleted)
        assertNull(reopenedTask.completedAt)
    }

    @Test
    fun testHasAnyTaskReturnsTrueOnlyWhenTaskExists() = runBlocking {
        assertFalse(taskRepository.hasAnyTask("u_empty"))

        val user = UserEntity(
            id = "u_empty",
            name = "User Empty",
            authProvider = AuthProvider.GUEST.name,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        db.userDao().upsertUser(user)

        taskRepository.upsertTask(Task(id = "t_has", userId = "u_empty", title = "Task 1"))
        assertTrue(taskRepository.hasAnyTask("u_empty"))
    }

    @Test
    fun testSubtaskToggleUpdatesParentTaskCompletedAtomically() = runBlocking {
        val user = UserEntity(
            id = "u1",
            name = "User 1",
            authProvider = AuthProvider.GUEST.name,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        db.userDao().upsertUser(user)

        val task = Task(
            id = "t1",
            userId = "u1",
            title = "Parent Task",
            scheduledDate = "2026-11-02",
            subtasks = listOf(
                Subtask(id = "s1", taskId = "t1", title = "Step 1", isCompleted = false),
                Subtask(id = "s2", taskId = "t1", title = "Step 2", isCompleted = false)
            )
        )
        taskRepository.upsertTask(task)

        // 1. Toggle Step 1 to true -> Task remains incomplete
        taskRepository.toggleSubtask("s1", true)
        var updatedTask = taskRepository.getTaskById("t1").first()
        assertNotNull(updatedTask)
        assertFalse(updatedTask!!.isCompleted)

        // 2. Toggle Step 2 to true -> All subtasks done -> Task is marked completed!
        taskRepository.toggleSubtask("s2", true)
        updatedTask = taskRepository.getTaskById("t1").first()
        assertTrue(updatedTask!!.isCompleted)
        assertNotNull(updatedTask.completedAt)

        // 3. Toggle Step 1 to false -> Task becomes incomplete again
        taskRepository.toggleSubtask("s1", false)
        updatedTask = taskRepository.getTaskById("t1").first()
        assertFalse(updatedTask!!.isCompleted)
        assertNull(updatedTask.completedAt)
    }

    @Test
    fun testRunningSessionCanBeFoundAndCompleted() = runBlocking {
        val user = UserEntity(
            id = "u1",
            name = "User 1",
            authProvider = AuthProvider.GUEST.name,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        db.userDao().upsertUser(user)

        val running = FocusSession(
            id = "fs1",
            userId = "u1",
            mode = FocusMode.POMODORO,
            plannedDurationSec = 1500,
            actualDurationSec = 0,
            status = FocusStatus.RUNNING,
            startedAt = 1000L,
            endedAt = null
        )
        focusSessionRepository.startSession(running)

        val foundRunning = focusSessionRepository.findRunningSession("u1").first()
        assertNotNull(foundRunning)
        assertEquals("fs1", foundRunning?.id)
        assertEquals(FocusStatus.RUNNING, foundRunning?.status)

        // Selesai sesi
        val completed = foundRunning!!.copy(
            actualDurationSec = 1500,
            status = FocusStatus.COMPLETED,
            endedAt = 2500L
        )
        focusSessionRepository.updateSession(completed)

        val noMoreRunning = focusSessionRepository.findRunningSession("u1").first()
        assertNull(noMoreRunning)
    }

    @Test
    fun testObserveTodayFocusMinutesWithDateRange() = runBlocking {
        val user = UserEntity(
            id = "u1",
            name = "User 1",
            authProvider = AuthProvider.GUEST.name,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        db.userDao().upsertUser(user)

        val today = LocalDate.of(2026, 11, 2)
        val sessionToday = FocusSession(
            id = "fs_today",
            userId = "u1",
            mode = FocusMode.POMODORO,
            plannedDurationSec = 1500,
            actualDurationSec = 3000, // 50 menit
            status = FocusStatus.COMPLETED,
            startedAt = fixedClock.millis(),
            endedAt = fixedClock.millis() + 3000_000L
        )
        focusSessionRepository.startSession(sessionToday)

        val minutes = focusSessionRepository.observeTodayFocusMinutes("u1", today).first()
        assertEquals(50, minutes)
    }

    @Test
    fun testConvertDistractionToTaskAtomic() = runBlocking {
        val user = UserEntity(
            id = "u1",
            name = "User 1",
            authProvider = AuthProvider.GUEST.name,
            targetDailyMinutes = 100,
            soundFxEnabled = true,
            createdAt = 1000L,
            updatedAt = 1000L
        )
        db.userDao().upsertUser(user)

        val note = DistractionNoteEntity(
            id = "n1",
            userId = "u1",
            content = "Ide penelitian skripsi",
            createdAt = 1000L,
            updatedAt = 1000L
        )
        db.distractionNoteDao().upsertNote(note)

        val newTaskId = distractionRepository.convertToTask("n1", Quadrant.Q2, "2026-11-03")
        assertNotNull(newTaskId)

        // Note harus ter-resolved dan converted_task_id terisi
        val resolvedNote = db.distractionNoteDao().getNoteById("n1")
        assertNotNull(resolvedNote)
        assertTrue(resolvedNote!!.isResolved)
        assertEquals(newTaskId, resolvedNote.convertedTaskId)

        // Task baru harus ada di Q2
        val createdTask = taskRepository.getTaskById(newTaskId).first()
        assertNotNull(createdTask)
        assertEquals("Ide penelitian skripsi", createdTask?.title)
        assertEquals(Quadrant.Q2, createdTask?.quadrant)
        assertEquals("2026-11-03", createdTask?.scheduledDate)
    }
}
