package com.example.fokus_cuy.feature.home

import androidx.compose.runtime.Immutable
import java.time.LocalTime
import kotlin.math.roundToInt

@Immutable
data class WeekDay(
    val dayName: String,
    val dateNum: String,
    val dateString: String = "" // yyyy-MM-dd
)

@Immutable
data class HomeUiState(
    val userName: String = "Sobat Fokus",
    val streakDays: Int = 0,
    val weekDays: List<WeekDay> = emptyList(),
    val selectedDayIndex: Int = 0,
    val selectedDate: String = "",
    val quotes: List<String> = listOf(
        "Tugas tebal itu cuma kumpulan tugas-tugas kecil yang lagi ngumpul. Pecah dulu, sikat satu-satu!",
        "Gak perlu sempurna, yang penting mulai. Lima menit pertama itu yang paling berat.",
        "Satu langkah kecil hari ini lebih berarti daripada rencana besar yang cuma dipikirin.",
        "Istirahat itu bagian dari proses, bukan hadiah setelah semuanya selesai.",
        "Progres kecil yang konsisten ngalahin semangat besar yang cuma sebentar."
    ),
    val quoteIndex: Int = 0,
    val progress: Float = 0f,
    val completedSteps: Int = 0,
    val totalSteps: Int = 0,
    val mainTaskId: String? = null,
    val mainTaskTitle: String = "Belum ada tugas hari ini",
    val mainTaskNextStep: String = "Tekan + untuk membuat tugas baru",
    val isMainTaskChecked: Boolean = false,
    val mainTaskFirstIncompleteSubtaskId: String? = null,
    val currentHour: Int = LocalTime.now().hour
) {
    val currentQuote: String
        get() = if (quotes.isNotEmpty()) quotes[quoteIndex % quotes.size] else ""

    /**
     * Sapaan Dinamis Berdasarkan Jam (DOKUMENTASI_SLICING_UI.md Section 4.1 point 2):
     * - Pagi (04:00 - 10:59)
     * - Siang/Sore (11:00 - 16:59)
     * - Malam (17:00 - 03:59)
     */
    val greetingTitle: String
        get() = when (currentHour) {
            in 4..10 -> "Pagi, $userName! Siap bikin hari ini produktif?"
            in 11..16 -> "Halo, $userName! Yuk, cicil dikit lagi sebelum santai."
            else -> "Malam, $userName! Selesaikan 1 langkah kecil, habis itu istirahat ya."
        }

    /**
     * Copywriting Adaptif Momentum Bar (DOKUMENTASI_SLICING_UI.md Section 4.2 point 3):
     * - 0%: "Mesin masih dingin nih. Yuk, centang langkah pertamamu!"
     * - 1-99%: "Mantap! Udah [X]% jalan menuju bebas tugas hari ini."
     * - 100%: "Gokil! Semua target hari ini ludes. Waktunya menikmati hari!"
     */
    val progressMotivationText: String
        get() = when {
            progress <= 0f -> "Mesin masih dingin nih. Yuk, centang langkah pertamamu!"
            progress >= 1f -> "Gokil! Semua target hari ini ludes. Waktunya menikmati hari!"
            else -> "Mantap! Udah ${(progress * 100f).roundToInt()}% jalan menuju bebas tugas hari ini."
        }
}
