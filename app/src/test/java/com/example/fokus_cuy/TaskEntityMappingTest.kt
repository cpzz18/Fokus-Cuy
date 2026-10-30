package com.example.fokus_cuy

import com.example.fokus_cuy.data.local.entity.TaskEntity
import com.example.fokus_cuy.domain.model.TaskItem
import com.example.fokus_cuy.ui.tasks.TaskUiState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TaskEntityMappingTest {

    @Test
    fun `toDomain converts TaskEntity to TaskItem properly`() {
        val entity = TaskEntity(
            id = 42L,
            title = "Kerjakan Fitur Fokus",
            description = "Setup SQLite dan Compose",
            isCompleted = true,
            createdAt = 1700000000000L
        )

        val domain = entity.toDomain()

        assertEquals(42L, domain.id)
        assertEquals("Kerjakan Fitur Fokus", domain.title)
        assertEquals("Setup SQLite dan Compose", domain.description)
        assertTrue(domain.isCompleted)
        assertEquals(1700000000000L, domain.createdAt)
    }

    @Test
    fun `fromDomain converts TaskItem to TaskEntity properly`() {
        val domain = TaskItem(
            id = 101L,
            title = "Review PR",
            description = "Senior Kotlin clean code",
            isCompleted = false,
            createdAt = 1700000050000L
        )

        val entity = TaskEntity.fromDomain(domain)

        assertEquals(101L, entity.id)
        assertEquals("Review PR", entity.title)
        assertEquals("Senior Kotlin clean code", entity.description)
        assertFalse(entity.isCompleted)
        assertEquals(1700000050000L, entity.createdAt)
    }

    @Test
    fun `toggle completion creates updated copy correctly`() {
        val initialTask = TaskItem(id = 1, title = "Membaca Buku", isCompleted = false)
        val completedTask = initialTask.copy(isCompleted = !initialTask.isCompleted)

        assertTrue(completedTask.isCompleted)
        assertEquals(initialTask.id, completedTask.id)
        assertEquals(initialTask.title, completedTask.title)
    }

    @Test
    fun `TaskUiState maintains expected defaults`() {
        val state = TaskUiState()

        assertTrue(state.tasks.isEmpty())
        assertEquals("", state.inputTitle)
        assertEquals("", state.inputDescription)
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
    }
}
