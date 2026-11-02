package com.example.fokus_cuy

import com.example.fokus_cuy.domain.StreakCalculator
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class StreakCalculatorTest {

    private val today = LocalDate.of(2026, 11, 2)

    @Test
    fun `empty focus days returns zero streak`() {
        val streak = StreakCalculator.calculate(emptySet(), today)
        assertEquals(0, streak)
    }

    @Test
    fun `focus today only returns 1`() {
        val focusDays = setOf(today)
        val streak = StreakCalculator.calculate(focusDays, today)
        assertEquals(1, streak)
    }

    @Test
    fun `focus yesterday only maintains 1 day streak even if today not yet completed`() {
        val focusDays = setOf(today.minusDays(1))
        val streak = StreakCalculator.calculate(focusDays, today)
        assertEquals(1, streak)
    }

    @Test
    fun `focus 3 consecutive days including today returns 3`() {
        val focusDays = setOf(
            today,
            today.minusDays(1),
            today.minusDays(2)
        )
        val streak = StreakCalculator.calculate(focusDays, today)
        assertEquals(3, streak)
    }

    @Test
    fun `focus 3 consecutive days ending yesterday returns 3`() {
        val focusDays = setOf(
            today.minusDays(1),
            today.minusDays(2),
            today.minusDays(3)
        )
        val streak = StreakCalculator.calculate(focusDays, today)
        assertEquals(3, streak)
    }

    @Test
    fun `gap in streak stops counting`() {
        // Today, yesterday, but missing 2 days ago, and present 3 days ago
        val focusDays = setOf(
            today,
            today.minusDays(1),
            today.minusDays(3)
        )
        val streak = StreakCalculator.calculate(focusDays, today)
        assertEquals(2, streak)
    }

    @Test
    fun `focus older than yesterday returns zero`() {
        val focusDays = setOf(today.minusDays(2))
        val streak = StreakCalculator.calculate(focusDays, today)
        assertEquals(0, streak)
    }

    @Test
    fun `streak across month boundary calculates correctly`() {
        val firstOfMonth = LocalDate.of(2026, 11, 1)
        val focusDays = setOf(
            firstOfMonth,
            LocalDate.of(2026, 10, 31),
            LocalDate.of(2026, 10, 30)
        )
        val streak = StreakCalculator.calculate(focusDays, firstOfMonth)
        assertEquals(3, streak)
    }
}
