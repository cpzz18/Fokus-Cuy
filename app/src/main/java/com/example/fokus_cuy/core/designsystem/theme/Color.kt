package com.example.fokus_cuy.core.designsystem.theme

import androidx.compose.ui.graphics.Color

/* ──────────────────────────────────────────────────────────────
 *  1. CHEERFUL SUNLIGHT PALETTE (DOKUMENTASI_SLICING_UI.md Section 2.1)
 * ────────────────────────────────────────────────────────────── */

/** `--color-sunburst` (#FFD026): Tombol CTA utama, FAB, indikator tab aktif */
val Sunburst = Color(0xFFFFD026)

/** `--color-marigold` (#FFAA00): Cincin timer Pomodoro, ikon streak, badge estimasi sesi */
val Marigold = Color(0xFFFFAA00)

/** `--color-custard` (#FFF6CC): Banner tugas aktif, kartu motivasi harian, highlight sub-tugas */
val Custard = Color(0xFFFFF6CC)

/** `--color-vanilla` (#FFFDF2): Latar belakang kanvas utama aplikasi */
val Vanilla = Color(0xFFFFFDF2)

/** `--color-warm-white` (#FFFFFF): Permukaan kartu, bottom sheet, dialog modal */
val WarmWhite = Color(0xFFFFFFFF)

/** `--color-warm-border` (#F5EED6): Garis batas (border) halus kontainer kartu */
val WarmBorder = Color(0xFFF5EED6)

/** `--color-espresso` (#1E1B18): Teks judul, angka display timer, label kontras tinggi */
val Espresso = Color(0xFF1E1B18)

/** `--color-umber` (#6B665C): Teks keterangan sekunder, subtitle, placeholder input */
val Umber = Color(0xFF6B665C)

/* ──────────────────────────────────────────────────────────────
 *  2. WARNA MATRIKS 4 KUADRAN (EISENHOWER) (Section 2.2)
 * ────────────────────────────────────────────────────────────── */

// Q1: Sikat Sekarang! (Penting & Mendesak)
val Q1Accent = Color(0xFFE64A19)
val Q1Bg = Color(0xFFFFEBE5)
val Q1Border = Color(0xFFFFCCBC)

// Q2: Cicil Terjadwal (Penting, Tidak Mendesak)
val Q2Accent = Color(0xFFD97706)
val Q2Bg = Color(0xFFFFF8E1)
val Q2Border = Color(0xFFFDE68A)

// Q3: Selesaikan Cepat (Mendesak, Tidak Penting)
val Q3Accent = Color(0xFF4D7C0F)
val Q3Bg = Color(0xFFF2FCE2)
val Q3Border = Color(0xFFD9F99D)

// Q4: Simpan di Kulkas (Tidak Mendesak & Tidak Penting)
val Q4Accent = Color(0xFF57534E)
val Q4Bg = Color(0xFFFAF8F5)
val Q4Border = Color(0xFFE7E5E4)

/* ──────────────────────────────────────────────────────────────
 *  3. ALIASES UNTUK KOMPATIBILITAS KODE
 * ────────────────────────────────────────────────────────────── */

val CreamBg = Vanilla
val CardWhite = WarmWhite
val YellowPrimary = Sunburst
val YellowBorder = Color(0xFFE6B80E)
val OrangeAccent = Marigold
val DarkText = Espresso
val GrayText = Umber
val LightBorder = WarmBorder
val DopamineCardBg = Custard
val DopamineBorder = Color(0xFFFDE68A)
val AmberText = Umber
val RedPillBg = Q1Bg
val RedPillBorder = Q1Border
val RedPillText = Q1Accent
val AiPillBg = Custard

val InkBg = Espresso
val InkBorder = Color(0x66FFAA00) // #FFAA00/40 (Section 4.1 point 2)
val StreakBg = Custard
val StreakBorder = Color(0xFFF5E1A4)
val IconButtonBg = WarmWhite
val TrackBg = Color(0xFFFFF8E1)
val TrackBorder = WarmBorder

// Splash Screen
val SplashBg = Color(0xFF0B0A09)
val SplashYellow = Sunburst
val SplashMuted = Color(0xFFB0AAA0)
val SplashFooter = Color(0xFF888279)
