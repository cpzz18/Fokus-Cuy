package com.example.fokus_cuy.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fokus_cuy.core.designsystem.modifier.pressScale
import com.example.fokus_cuy.core.designsystem.theme.CardWhite

/**
 * Surface interaktif dengan umpan balik skala pegas saat ditekan.
 */
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
