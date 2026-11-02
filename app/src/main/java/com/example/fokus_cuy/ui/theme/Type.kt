package com.example.fokus_cuy.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.fokus_cuy.core.designsystem.theme.Espresso

/**
 * Standar Tipografi FokusCuy (DOKUMENTASI_SLICING_UI.md Section 2.3).
 * - Judul & Display: Bold 700 / ExtraBold 800
 * - Konten & Form: Regular 400 / Medium 500 / SemiBold 600
 */
val Typography = Typography(
    // Display: Timer Countdown 52px - 56px (ExtraBold)
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 56.sp,
        lineHeight = 64.sp,
        color = Espresso
    ),
    // H1 / Header Halaman: 18px - 20px (Bold)
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.3).sp,
        color = Espresso
    ),
    // H2 / Judul Seksi: 15px - 16px (Bold)
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.1).sp,
        color = Espresso
    ),
    // Judul Tugas: 14px - 15px (SemiBold)
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 15.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.2).sp,
        color = Espresso
    ),
    // Sub-tugas & Isi: 12px - 13px (Medium)
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 19.sp,
        letterSpacing = 0.1.sp,
        color = Espresso
    ),
    // Badge, Tag, Caption: 10px - 11px (Bold / SemiBold)
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 0.1.sp,
        color = Espresso
    )
)
