package com.example.fokus_cuy.core.designsystem.icon

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SunIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.1f
        val center = Offset(w * 0.5f, h * 0.5f)
        drawCircle(color = color, radius = w * 0.22f, center = center, style = Stroke(width = stroke))

        for (i in 0 until 8) {
            val angle = Math.toRadians((i * 45).toDouble())
            val startR = w * 0.34f
            val endR = w * 0.46f
            drawLine(
                color = color,
                start = Offset(center.x + (startR * cos(angle)).toFloat(), center.y + (startR * sin(angle)).toFloat()),
                end = Offset(center.x + (endR * cos(angle)).toFloat(), center.y + (endR * sin(angle)).toFloat()),
                strokeWidth = stroke,
                cap = StrokeCap.Round
            )
        }
    }
}

@Composable
fun GridIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.09f
        val boxSize = Size(w * 0.34f, h * 0.34f)
        val positions = listOf(
            Offset(w * 0.12f, h * 0.12f),
            Offset(w * 0.54f, h * 0.12f),
            Offset(w * 0.12f, h * 0.54f),
            Offset(w * 0.54f, h * 0.54f)
        )
        positions.forEach { topLeft ->
            drawRoundRect(
                color = color,
                topLeft = topLeft,
                size = boxSize,
                cornerRadius = CornerRadius(w * 0.08f),
                style = Stroke(width = stroke)
            )
        }
    }
}

@Composable
fun StopwatchIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.09f
        drawCircle(color = color, radius = w * 0.38f, center = Offset(w * 0.5f, h * 0.55f), style = Stroke(width = stroke))
        drawLine(color = color, start = Offset(w * 0.38f, h * 0.1f), end = Offset(w * 0.62f, h * 0.1f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color = color, start = Offset(w * 0.5f, h * 0.55f), end = Offset(w * 0.64f, h * 0.42f), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

@Composable
fun ChartIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.09f
        val axis = Path().apply {
            moveTo(w * 0.15f, h * 0.15f)
            lineTo(w * 0.15f, h * 0.85f)
            lineTo(w * 0.85f, h * 0.85f)
        }
        drawPath(axis, color = color, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))
        drawLine(color = color, start = Offset(w * 0.35f, h * 0.85f), end = Offset(w * 0.35f, h * 0.55f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color = color, start = Offset(w * 0.55f, h * 0.85f), end = Offset(w * 0.55f, h * 0.32f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color = color, start = Offset(w * 0.75f, h * 0.85f), end = Offset(w * 0.75f, h * 0.48f), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

@Composable
fun LayersIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.11f

        val topLayer = Path().apply {
            moveTo(w * 0.5f, h * 0.15f)
            lineTo(w * 0.9f, h * 0.35f)
            lineTo(w * 0.5f, h * 0.55f)
            lineTo(w * 0.1f, h * 0.35f)
            close()
        }
        drawPath(topLayer, color = color, style = Stroke(width = stroke, join = StrokeJoin.Round))

        val midLayer = Path().apply {
            moveTo(w * 0.1f, h * 0.55f)
            lineTo(w * 0.5f, h * 0.75f)
            lineTo(w * 0.9f, h * 0.55f)
        }
        drawPath(midLayer, color = color, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))

        val botLayer = Path().apply {
            moveTo(w * 0.1f, h * 0.75f)
            lineTo(w * 0.5f, h * 0.95f)
            lineTo(w * 0.9f, h * 0.75f)
        }
        drawPath(botLayer, color = color, style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun ClockIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.11f
        drawCircle(color = color, radius = w * 0.42f, style = Stroke(width = stroke))
        drawLine(color = color, start = Offset(w * 0.5f, h * 0.5f), end = Offset(w * 0.5f, h * 0.28f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color = color, start = Offset(w * 0.5f, h * 0.5f), end = Offset(w * 0.68f, h * 0.5f), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}
