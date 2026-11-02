package com.example.fokus_cuy.core.designsystem.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Dimensi dan Spacing Standar FokusCuy (DOKUMENTASI_SLICING_UI.md).
 * Menghilangkan magic numbers di seluruh layer UI.
 */
object Dimens {
    val ScreenHorizontalPadding: Dp = 18.dp
    val ScreenVerticalPadding: Dp = 12.dp

    // Karakter Sudut (Border Radius) - Section 1
    val CardCornerRadius: Dp = 22.dp       // Kartu Utama & Kontainer: 20px - 22px
    val ModalCornerRadius: Dp = 28.dp      // Modal / Bottom Sheet: 28px
    val ActionButtonRadius: Dp = 16.dp     // Tombol Aksi & Kartu Sub-tugas: 14px - 16px
    val CardPadding: Dp = 18.dp
    val CardElevation: Dp = 1.dp

    val SectionSpacing: Dp = 16.dp
    val ItemSpacing: Dp = 12.dp
    val ContentBottomPadding: Dp = 90.dp

    val IconButtonSize: Dp = 36.dp
    val ActionCircleSize: Dp = 32.dp
    val PlayButtonSize: Dp = 44.dp
    val CheckboxSize: Dp = 28.dp

    val PillPaddingHorizontal: Dp = 10.dp
    val PillPaddingVertical: Dp = 4.dp
}
