package com.example.fokus_cuy.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.fokus_cuy.core.designsystem.icon.ChartIcon
import com.example.fokus_cuy.core.designsystem.icon.GridIcon
import com.example.fokus_cuy.core.designsystem.icon.StopwatchIcon
import com.example.fokus_cuy.core.designsystem.icon.SunIcon

enum class AppDestination(val label: String) {
    TODAY("Hari Ini") {
        @Composable
        override fun Icon(size: Dp, color: Color) = SunIcon(size = size, color = color)
    },
    MATRIX("Matriks") {
        @Composable
        override fun Icon(size: Dp, color: Color) = GridIcon(size = size, color = color)
    },
    FOCUS("Fokus") {
        @Composable
        override fun Icon(size: Dp, color: Color) = StopwatchIcon(size = size, color = color)
    },
    STATS("Statistik") {
        @Composable
        override fun Icon(size: Dp, color: Color) = ChartIcon(size = size, color = color)
    };

    @Composable
    abstract fun Icon(size: Dp, color: Color)
}
