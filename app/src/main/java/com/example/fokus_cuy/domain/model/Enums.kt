package com.example.fokus_cuy.domain.model

enum class Quadrant {
    Q1, // Do First (Penting & Mendesak)
    Q2, // Schedule (Penting & Tidak Mendesak)
    Q3, // Delegate (Tidak Penting & Mendesak)
    Q4  // Eliminate (Tidak Penting & Tidak Mendesak)
}

enum class TaskSource {
    MANUAL,
    AI_PDF
}

enum class FocusMode {
    POMODORO,
    TWO_MINUTE,
    BREAK
}

enum class FocusStatus {
    RUNNING,
    COMPLETED,
    PAUSED,
    CANCELLED
}

enum class AuthProvider {
    EMAIL,
    GOOGLE_CAMPUS,
    GUEST
}
