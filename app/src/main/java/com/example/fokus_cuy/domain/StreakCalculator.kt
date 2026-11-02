package com.example.fokus_cuy.domain

import java.time.LocalDate

object StreakCalculator {
    /**
     * Menghitung streak hari fokus berturut-turut.
     * Aturan:
     * - Jika [today] ada di [focusDays], hitung mundur berturut-turut mulai [today].
     * - Jika [today] belum terisi tapi [today - 1] ada di [focusDays], streak kemarin dipertahankan.
     * - Jika keduanya tidak ada, streak = 0.
     */
    fun calculate(focusDays: Set<LocalDate>, today: LocalDate): Int {
        if (focusDays.isEmpty()) return 0

        var cursor = if (focusDays.contains(today)) {
            today
        } else if (focusDays.contains(today.minusDays(1))) {
            today.minusDays(1)
        } else {
            return 0
        }

        var streak = 0
        while (focusDays.contains(cursor)) {
            streak++
            cursor = cursor.minusDays(1)
        }
        return streak
    }
}
