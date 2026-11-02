package com.example.fokus_cuy.feature.home.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fokus_cuy.core.designsystem.component.IconCircleButton
import com.example.fokus_cuy.core.designsystem.component.PressableSurface
import com.example.fokus_cuy.core.designsystem.icon.CompassIcon
import com.example.fokus_cuy.core.designsystem.icon.FlameIcon
import com.example.fokus_cuy.core.designsystem.icon.FokusLogoIcon
import com.example.fokus_cuy.core.designsystem.icon.SpeakerIcon
import com.example.fokus_cuy.core.designsystem.modifier.entrance
import com.example.fokus_cuy.core.designsystem.modifier.innerHighlight
import com.example.fokus_cuy.core.designsystem.theme.Custard
import com.example.fokus_cuy.core.designsystem.theme.Dimens
import com.example.fokus_cuy.core.designsystem.theme.Espresso
import com.example.fokus_cuy.core.designsystem.theme.InkBg
import com.example.fokus_cuy.core.designsystem.theme.InkBorder
import com.example.fokus_cuy.core.designsystem.theme.Marigold
import com.example.fokus_cuy.core.designsystem.theme.Sunburst
import com.example.fokus_cuy.core.designsystem.theme.Umber

/**
 * Header Halaman Home (DOKUMENTASI_SLICING_UI.md Section 4.1 point 2).
 * - Baris Brand (Atas): Kapsul #1E1B18 berbatas border tipis #FFAA00/40.
 * - Lencana Streak: Pill berlatar #FFF6CC dengan ikon api #FFAA00.
 * - Baris Sapaan (Bawah): Dinamis sesuai waktu setempat.
 */
@Composable
fun HomeHeader(
    greetingTitle: String,
    streakDays: Int,
    onLogoClick: () -> Unit = {},
    onStreakClick: () -> Unit = {},
    onCompassClick: () -> Unit = {},
    onSpeakerClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val flicker by rememberInfiniteTransition(label = "flame").animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flameScale"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .entrance(0)
            .padding(horizontal = Dimens.ScreenHorizontalPadding, vertical = Dimens.ScreenVerticalPadding)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo Brand Capsule (#1E1B18 dengan border #FFAA00/40)
            PressableSurface(
                onClick = onLogoClick,
                color = InkBg,
                shape = RoundedCornerShape(50),
                border = BorderStroke(1.dp, InkBorder),
                pressedScale = 0.96f
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .innerHighlight(alpha = 0.12f)
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    FokusLogoIcon(size = 20.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "FOKUS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White,
                        letterSpacing = 0.3.sp
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "CUY",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Sunburst,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Streak & Action Buttons
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Lencana Streak (#FFF6CC dengan api #FFAA00)
                PressableSurface(
                    onClick = onStreakClick,
                    color = Custard,
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, Color(0xFFF5E1A4)),
                    pressedScale = 0.94f
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .innerHighlight(alpha = 0.8f)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier.graphicsLayer {
                                scaleX = flicker
                                scaleY = flicker
                                transformOrigin = TransformOrigin(0.5f, 1f)
                            }
                        ) {
                            FlameIcon(size = 15.dp, color = Marigold)
                        }
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "$streakDays Hari",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Espresso,
                            letterSpacing = 0.1.sp
                        )
                    }
                }

                IconCircleButton(onClick = onCompassClick) {
                    CompassIcon(size = 18.dp, color = Marigold)
                }

                IconCircleButton(onClick = onSpeakerClick) {
                    SpeakerIcon(size = 17.dp, color = Umber)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Baris Sapaan Dinamis
        Text(
            text = greetingTitle,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Espresso,
            letterSpacing = (-0.3).sp,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(5.dp))

        Text(
            text = "Yuk, cicil langkah kecilmu hari ini agar target tercapai tanpa beban.",
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = Umber,
            lineHeight = 19.sp,
            letterSpacing = 0.1.sp
        )
    }
}
