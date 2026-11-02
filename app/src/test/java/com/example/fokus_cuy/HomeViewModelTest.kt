package com.example.fokus_cuy

import com.example.fokus_cuy.feature.home.HomeUiState
import com.example.fokus_cuy.feature.home.HomeViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel()
    }

    @Test
    fun `initial uiState has expected default values`() {
        val state = viewModel.uiState.value

        assertEquals("Dimas", state.userName)
        assertEquals(5, state.streakDays)
        assertEquals(6, state.selectedDayIndex)
        assertEquals(0, state.quoteIndex)
        assertFalse(state.isMainTaskChecked)
        assertTrue(state.currentQuote.isNotBlank())
        assertEquals(7, state.weekDays.size)
    }

    @Test
    fun `selectDay updates selectedDayIndex properly`() {
        viewModel.selectDay(2)

        assertEquals(2, viewModel.uiState.value.selectedDayIndex)
    }

    @Test
    fun `nextQuote advances index and wraps around properly`() {
        val quotesCount = viewModel.uiState.value.quotes.size
        assertTrue(quotesCount > 0)

        viewModel.nextQuote()
        assertEquals(1, viewModel.uiState.value.quoteIndex)

        repeat(quotesCount - 1) {
            viewModel.nextQuote()
        }
        assertEquals(0, viewModel.uiState.value.quoteIndex)
    }

    @Test
    fun `toggleMainTask toggles checked state properly`() {
        assertFalse(viewModel.uiState.value.isMainTaskChecked)

        viewModel.toggleMainTask()
        assertTrue(viewModel.uiState.value.isMainTaskChecked)

        viewModel.toggleMainTask()
        assertFalse(viewModel.uiState.value.isMainTaskChecked)
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
}
