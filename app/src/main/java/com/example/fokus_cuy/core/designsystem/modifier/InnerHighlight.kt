package com.example.fokus_cuy.core.designsystem.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Garis cahaya 1dp di tepi atas bagian dalam — memberi kedalaman fisik tactile.
 */
fun Modifier.innerHighlight(alpha: Float = 0.8f, corner: Dp? = null): Modifier =
    drawWithContent {
        drawContent()
        val inset = 0.5.dp.toPx()
        val w = size.width - inset * 2
        val h = size.height - inset * 2
        if (w <= 0f || h <= 0f) return@drawWithContent
        val radius = corner?.toPx() ?: (size.minDimension / 2f)
        drawRoundRect(
            brush = Brush.verticalGradient(
                colors = listOf(Color.White.copy(alpha = alpha), Color.Transparent),
                startY = 0f,
                endY = size.height * 0.6f
            ),
            topLeft = Offset(inset, inset),
            size = Size(w, h),
            cornerRadius = CornerRadius(radius),
            style = Stroke(width = 1.dp.toPx())
        )
    }
