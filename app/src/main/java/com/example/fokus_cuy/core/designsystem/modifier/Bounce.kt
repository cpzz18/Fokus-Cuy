package com.example.fokus_cuy.core.designsystem.modifier

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * Skala pegas saat elemen disentuh/ditekan.
 */
@Composable
fun MutableInteractionSource.pressScale(pressedScale: Float): State<Float> {
    val pressed by collectIsPressedAsState()
    return animateFloatAsState(
        targetValue = if (pressed) pressedScale else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessMedium),
        label = "pressScale"
    )
}

/**
 * Clickable modifier dengan animasi bounce halus serta ripple sesuai [shape].
 */
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
