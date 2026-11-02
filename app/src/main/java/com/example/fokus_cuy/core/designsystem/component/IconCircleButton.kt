package com.example.fokus_cuy.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fokus_cuy.core.designsystem.theme.Dimens
import com.example.fokus_cuy.core.designsystem.theme.IconButtonBg
import com.example.fokus_cuy.core.designsystem.theme.LightBorder

/**
 * Tombol aksi bulat reusable dengan border dan elevation tactile.
 */
@Composable
fun IconCircleButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = Dimens.IconButtonSize,
    color: Color = IconButtonBg,
    borderColor: Color = LightBorder,
    shadowElevation: Dp = 1.dp,
    pressedScale: Float = 0.88f,
    content: @Composable () -> Unit
) {
    PressableSurface(
        onClick = onClick,
        color = color,
        shape = CircleShape,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = shadowElevation,
        pressedScale = pressedScale,
        modifier = modifier.size(size)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}
