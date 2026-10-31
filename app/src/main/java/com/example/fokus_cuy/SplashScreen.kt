package com.example.fokus_cuy

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.PI
import kotlin.math.cos
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SplashScreen() {
    // Tiap elemen punya "napas" sendiri supaya masuknya berjenjang, bukan serentak.
    val logoAlpha = remember { Animatable(0f) }
    val logoScale = remember { Animatable(0.86f) }
    val wordmark = remember { Animatable(0f) }
    val tagline = remember { Animatable(0f) }
    val footer = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch { logoAlpha.animateTo(1f, tween(750, easing = FastOutSlowInEasing)) }
        launch {
            logoScale.animateTo(
                1f,
                spring(dampingRatio = 0.72f, stiffness = Spring.StiffnessLow)
            )
        }
        launch { delay(260); wordmark.animateTo(1f, tween(700, easing = FastOutSlowInEasing)) }
        launch { delay(620); tagline.animateTo(1f, tween(700, easing = FastOutSlowInEasing)) }
        launch { delay(900); footer.animateTo(1f, tween(800)) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0B0A09))
            // Pendar hangat sangat tipis di belakang logo (bukan neon)
            .drawBehind {
                val c = Offset(size.width / 2f, size.height / 2f - 24.dp.toPx())
                val r = 260.dp.toPx()
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFFFFDE17).copy(alpha = 0.07f * logoAlpha.value),
                            Color.Transparent
                        ),
                        center = c,
                        radius = r
                    ),
                    radius = r,
                    center = c
                )
            },
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                FokusLogoIcon(
                    modifier = Modifier.graphicsLayer {
                        alpha = logoAlpha.value
                        scaleX = logoScale.value
                        scaleY = logoScale.value
                    },
                    size = 54.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "FOKUS",
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.5.sp,
                    modifier = Modifier.graphicsLayer {
                        alpha = wordmark.value
                        translationX = (1f - wordmark.value) * -14.dp.toPx()
                    }
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "CUY",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFFDE17),
                    modifier = Modifier
                        .graphicsLayer {
                            alpha = wordmark.value
                            translationX = (1f - wordmark.value) * -22.dp.toPx()
                        }
                        .padding(top = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Plan Less. Focus Better.",
                fontSize = 14.sp,
                letterSpacing = 0.4.sp,
                color = Color(0xFFB0AAA0),
                modifier = Modifier.graphicsLayer {
                    alpha = tagline.value
                    translationY = (1f - tagline.value) * 6.dp.toPx()
                }
            )

            Spacer(modifier = Modifier.height(36.dp))

            PulseDots(
                color = Color(0xFFFFDE17),
                modifier = Modifier.graphicsLayer { alpha = tagline.value }
            )
        }

        Text(
            text = "Mulai langkah kecilmu hari ini.",
            fontSize = 12.sp,
            letterSpacing = 0.2.sp,
            color = Color(0xFF888279),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
                .graphicsLayer { alpha = footer.value }
        )
    }
}

/** Indikator loading: tiga titik yang berdenyut bergelombang, lebih tenang daripada spinner. */
@Composable
private fun PulseDots(color: Color, modifier: Modifier = Modifier) {
    val phase = rememberInfiniteTransition(label = "dots").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(1200, easing = LinearEasing)),
        label = "dotsPhase"
    )

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(7.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(3) { i ->
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .graphicsLayer {
                        val p = (phase.value + i * 0.16f) % 1f
                        val wave = 0.5f - 0.5f * cos(2f * PI.toFloat() * p) // 0..1
                        val s = 0.7f + 0.5f * wave
                        scaleX = s
                        scaleY = s
                        alpha = 0.3f + 0.7f * wave
                    }
                    .background(color, CircleShape)
            )
        }
    }
}