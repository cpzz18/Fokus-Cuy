package com.example.fokus_cuy.core.designsystem.icon

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Logo resmi FokusCuy — TIDAK DIUBAH geometri gambarnya.
 */
@Composable
fun FokusLogoIcon(
    modifier: Modifier = Modifier,
    size: Dp = 52.dp,
    arcColor: Color = Color(0xFFFFDE17),
    innerColor: Color = Color.White
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val strokeWidth = w * 0.18f

        drawArc(
            color = arcColor,
            startAngle = 50f,
            sweepAngle = 260f,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Butt),
            size = Size(w * 0.82f, h * 0.82f),
            topLeft = Offset(w * 0.09f, h * 0.09f)
        )

        drawCircle(
            color = innerColor,
            radius = w * 0.12f,
            center = Offset(w * 0.48f, h * 0.5f)
        )

        val chevronPath = Path().apply {
            moveTo(w * 0.72f, h * 0.34f)
            lineTo(w * 0.90f, h * 0.50f)
            lineTo(w * 0.72f, h * 0.66f)
        }
        drawPath(
            path = chevronPath,
            color = innerColor,
            style = Stroke(width = w * 0.14f, cap = StrokeCap.Square)
        )
    }
}
