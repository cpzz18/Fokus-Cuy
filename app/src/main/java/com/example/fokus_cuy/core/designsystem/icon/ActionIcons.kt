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
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import com.example.fokus_cuy.core.designsystem.theme.OrangeAccent
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun FlameIcon(size: Dp, color: Color = OrangeAccent) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val outer = Path().apply {
            moveTo(w * 0.5f, h * 0.05f)
            cubicTo(w * 0.9f, h * 0.35f, w * 0.95f, h * 0.75f, w * 0.5f, h * 0.95f)
            cubicTo(w * 0.05f, h * 0.75f, w * 0.1f, h * 0.35f, w * 0.5f, h * 0.05f)
            close()
        }
        drawPath(path = outer, color = color)

        val inner = Path().apply {
            moveTo(w * 0.5f, h * 0.44f)
            cubicTo(w * 0.69f, h * 0.58f, w * 0.69f, h * 0.78f, w * 0.5f, h * 0.89f)
            cubicTo(w * 0.31f, h * 0.78f, w * 0.31f, h * 0.58f, w * 0.5f, h * 0.44f)
            close()
        }
        drawPath(path = inner, color = Color(0xFFFFE08A))
    }
}

@Composable
fun CompassIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.1f
        drawCircle(color = color, radius = w * 0.42f, style = Stroke(width = stroke))
        val needle = Path().apply {
            moveTo(w * 0.62f, h * 0.38f)
            lineTo(w * 0.54f, h * 0.54f)
            lineTo(w * 0.38f, h * 0.62f)
            lineTo(w * 0.46f, h * 0.46f)
            close()
        }
        drawPath(needle, color = color)
        drawPath(
            needle,
            color = color,
            style = Stroke(width = w * 0.05f, join = StrokeJoin.Round)
        )
    }
}

@Composable
fun SpeakerIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.1f
        val path = Path().apply {
            moveTo(w * 0.15f, h * 0.38f)
            lineTo(w * 0.32f, h * 0.38f)
            lineTo(w * 0.55f, h * 0.20f)
            lineTo(w * 0.55f, h * 0.80f)
            lineTo(w * 0.32f, h * 0.62f)
            lineTo(w * 0.15f, h * 0.62f)
            close()
        }
        drawPath(path, color = color, style = Stroke(width = stroke, join = StrokeJoin.Round))
        drawArc(
            color = color,
            startAngle = -45f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
            topLeft = Offset(w * 0.45f, h * 0.28f),
            size = Size(w * 0.35f, h * 0.44f)
        )
    }
}

@Composable
fun SparkleIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w * 0.5f, 0f)
            quadraticTo(w * 0.5f, h * 0.5f, w, h * 0.5f)
            quadraticTo(w * 0.5f, h * 0.5f, w * 0.5f, h)
            quadraticTo(w * 0.5f, h * 0.5f, 0f, h * 0.5f)
            quadraticTo(w * 0.5f, h * 0.5f, w * 0.5f, 0f)
            close()
        }
        drawPath(path, color = color, style = Stroke(width = w * 0.12f, join = StrokeJoin.Round))
    }
}

private fun DrawScope.arrowHead(
    tip: Offset,
    direction: Float,
    length: Float,
    stroke: Float,
    color: Color
) {
    val spread = 0.62f
    val back = direction + PI.toFloat()
    val p1 = Offset(tip.x + length * cos(back + spread), tip.y + length * sin(back + spread))
    val p2 = Offset(tip.x + length * cos(back - spread), tip.y + length * sin(back - spread))
    drawLine(color, tip, p1, strokeWidth = stroke, cap = StrokeCap.Round)
    drawLine(color, tip, p2, strokeWidth = stroke, cap = StrokeCap.Round)
}

@Composable
fun RefreshIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val stroke = w * 0.12f
        drawArc(
            color = color,
            startAngle = -160f,
            sweepAngle = 140f,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
            topLeft = Offset(w * 0.15f, h * 0.15f),
            size = Size(w * 0.7f, h * 0.7f)
        )
        drawArc(
            color = color,
            startAngle = 20f,
            sweepAngle = 140f,
            useCenter = false,
            style = Stroke(width = stroke, cap = StrokeCap.Round),
            topLeft = Offset(w * 0.15f, h * 0.15f),
            size = Size(w * 0.7f, h * 0.7f)
        )

        val center = Offset(w * 0.5f, h * 0.5f)
        val r = w * 0.35f
        listOf(-20f, 160f).forEach { deg ->
            val a = Math.toRadians(deg.toDouble()).toFloat()
            val tip = Offset(center.x + r * cos(a), center.y + r * sin(a))
            arrowHead(
                tip = tip,
                direction = a + (PI / 2).toFloat(),
                length = w * 0.22f,
                stroke = stroke,
                color = color
            )
        }
    }
}

@Composable
fun PlayTriangleIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w * 0.28f, h * 0.17f)
            lineTo(w * 0.84f, h * 0.5f)
            lineTo(w * 0.28f, h * 0.83f)
            close()
        }
        drawPath(path, color = color)
        drawPath(path, color = color, style = Stroke(width = w * 0.1f, join = StrokeJoin.Round))
    }
}

@Composable
fun ChevronRightIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w * 0.35f, h * 0.2f)
            lineTo(w * 0.65f, h * 0.5f)
            lineTo(w * 0.35f, h * 0.8f)
        }
        drawPath(path, color = color, style = Stroke(width = w * 0.12f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}

@Composable
fun CheckIcon(size: Dp, color: Color) {
    Canvas(modifier = Modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height
        val path = Path().apply {
            moveTo(w * 0.24f, h * 0.53f)
            lineTo(w * 0.42f, h * 0.70f)
            lineTo(w * 0.76f, h * 0.32f)
        }
        drawPath(path, color = color, style = Stroke(width = w * 0.14f, cap = StrokeCap.Round, join = StrokeJoin.Round))
    }
}
