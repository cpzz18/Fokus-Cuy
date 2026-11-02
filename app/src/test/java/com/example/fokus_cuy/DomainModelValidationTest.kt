package com.example.fokus_cuy

import com.example.fokus_cuy.domain.model.DistractionNote
import com.example.fokus_cuy.domain.model.FocusSession
import com.example.fokus_cuy.domain.model.Subtask
import com.example.fokus_cuy.domain.model.Task
import com.example.fokus_cuy.domain.model.User
import org.junit.Test

class DomainModelValidationTest {

    @Test(expected = IllegalArgumentException::class)
    fun `user with blank name throws`() {
        User(id = "u1", name = "   ")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `task with blank title throws`() {
        Task(id = "t1", userId = "u1", title = "")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `task with invalid scheduledDate throws`() {
        Task(id = "t1", userId = "u1", title = "Task", scheduledDate = "02-11-2026")
    }

    @Test(expected = IllegalArgumentException::class)
    fun `subtask with pomodoro estimate out of range throws`() {
        Subtask(id = "s1", taskId = "t1", title = "Subtask", pomodoroEstimate = 5)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `focus session with endedAt before startedAt throws`() {
        FocusSession(
            id = "fs1",
            userId = "u1",
            startedAt = 1000L,
            endedAt = 500L
        )
    }

    @Test(expected = IllegalArgumentException::class)
    fun `distraction note with empty content throws`() {
        DistractionNote(id = "d1", userId = "u1", content = "")
    }
}
