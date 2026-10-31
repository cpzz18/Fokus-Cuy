package com.example.fokus_cuy

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/* ──────────────────────────────────────────────────────────────
 *  PALET WARNA — "warm tactile minimalism"
 *  Semua permukaan condong ke krem/gading hangat, border tipis,
 *  teks memakai cokelat-tinta (bukan hitam murni / abu dingin).
 * ────────────────────────────────────────────────────────────── */
val CreamBg = Color(0xFFFAF6EC)
val CardWhite = Color(0xFFFFFEFA)          // gading hangat, bukan putih murni
val YellowPrimary = Color(0xFFFACC15)
val YellowBorder = Color(0xFFE6B80E)       // tepi tombol kuning (memberi kesan "padat")
val OrangeAccent = Color(0xFFF59E0B)
val DarkText = Color(0xFF1F1B16)
val GrayText = Color(0xFF7A7166)
val LightBorder = Color(0xFFEDE7D8)
val DopamineCardBg = Color(0xFFFEF3C7)
val DopamineBorder = Color(0xFFFDE68A)
val AmberText = Color(0xFF8C7A55)
val RedPillBg = Color(0xFFFFE4E2)
val RedPillBorder = Color(0xFFFECDD3)
val RedPillText = Color(0xFFD9381E)
val AiPillBg = Color(0xFFFEF3C7)

val InkBg = Color(0xFF141413)
val InkBorder = Color(0xFF3A3528)
val StreakBg = Color(0xFFFEF5D8)
val StreakBorder = Color(0xFFF5E1A4)
val IconButtonBg = Color(0xFFFFFDF9)
val TrackBg = Color(0xFFFAF6E8)
val TrackBorder = Color(0xFFF3E8C8)

/* ──────────────────────────────────────────────────────────────
 *  HELPER INTERAKSI & ANIMASI
 * ────────────────────────────────────────────────────────────── */

/** Skala "tekan" berbasis pegas: menciut saat ditekan, memantul tipis saat dilepas. */
@Composable
fun MutableInteractionSource.pressScale(pressedScale: Float): State<Float> {
    val pressed by collectIsPressedAsState()
    return animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "pressScale"
    )
}

/** Clickable dengan bounce halus + ripple yang terpotong rapi mengikuti [shape]. */
fun Modifier.bounceClick(
    pressedScale: Float = 0.94f,
    shape: Shape = RoundedCornerShape(12.dp),
    onClick: () -> Unit = {}
): Modifier = composed {
    val source = remember { MutableInteractionSource() }
    val scale by source.pressScale(pressedScale)
    this
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .clip(shape)
        .clickable(
            interactionSource = source,
            indication = LocalIndication.current,
            onClick = onClick
        )
}

/** Surface yang bisa diklik: bounce + ripple bawaan Material, bayangan tidak ikut terpotong. */
@Composable
fun PressableSurface(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    color: Color = CardWhite,
    border: BorderStroke? = null,
    shadowElevation: Dp = 0.dp,
    pressedScale: Float = 0.98f,
    content: @Composable () -> Unit
) {
    val source = remember { MutableInteractionSource() }
    val scale by source.pressScale(pressedScale)
    Surface(
        onClick = onClick,
        modifier = modifier.graphicsLayer {
            scaleX = scale
            scaleY = scale
        },
        shape = shape,
        color = color,
        border = border,
        shadowElevation = shadowElevation,
        interactionSource = source,
        content = content
    )
}

/**
 * Garis cahaya 1dp di tepi atas bagian dalam — membuat permukaan terasa
 * sedikit "membulat" seperti tombol fisik, tanpa gradien mencolok.
 * Letakkan SEBELUM padding di rantai modifier agar mengikuti batas penuh.
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

/** Animasi masuk berjenjang: fade + naik 18dp, tertunda 60ms per [index]. */
fun Modifier.entrance(index: Int): Modifier = composed {
    val progress = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 520,
                delayMillis = 60 * index,
                easing = FastOutSlowInEasing
            )
        )
    }
    graphicsLayer {
        alpha = progress.value
        translationY = (1f - progress.value) * 18.dp.toPx()
    }
}

/* ──────────────────────────────────────────────────────────────
 *  IKON LOGO — TIDAK DIUBAH
 * ────────────────────────────────────────────────────────────── */
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

/* ──────────────────────────────────────────────────────────────
 *  IKON LAIN — goresan dihaluskan (sudut membulat, kepala panah, dsb.)
 * ────────────────────────────────────────────────────────────── */
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

        // Inti api yang lebih terang memberi kedalaman
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
        // Stroke tipis dengan join membulat → ujung jarum tidak lancip kaku
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

/** Kepala panah kecil: [direction] = arah gerak dalam radian. */
private fun DrawScope.arrowHead(
    tip: Offset,
    direction: Float,
    length: Float,
    stroke: Float,
    color: Color
) {
    val spread = 0.62f // ± ~35°
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

        // Kepala panah di ujung tiap busur (searah jarum jam)
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
        // Sudut dibulatkan supaya terasa lembut, bukan segitiga tajam
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