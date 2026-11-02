package com.example.fokus_cuy.feature.home.component

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fokus_cuy.core.designsystem.modifier.entrance
import com.example.fokus_cuy.core.designsystem.modifier.innerHighlight
import com.example.fokus_cuy.core.designsystem.theme.Dimens
import com.example.fokus_cuy.core.designsystem.theme.Espresso
import com.example.fokus_cuy.core.designsystem.theme.Marigold
import com.example.fokus_cuy.core.designsystem.theme.TrackBg
import com.example.fokus_cuy.core.designsystem.theme.Umber
import com.example.fokus_cuy.core.designsystem.theme.Vanilla
import com.example.fokus_cuy.core.designsystem.theme.WarmBorder
import com.example.fokus_cuy.core.designsystem.theme.WarmWhite
import kotlin.math.roundToInt

/**
 * Daily Momentum Bar (DOKUMENTASI_SLICING_UI.md Section 4.2 point 3).
 * - Progress bar tebal berlatar #FFF8E1 dengan isi #FFAA00 (Marigold).
 * - Copywriting adaptif sesuai persentase tuntas.
 */
@Composable
fun FocusProgressCard(
    progressValue: Float,
    completedSteps: Int,
    totalSteps: Int,
    motivationText: String,
    modifier: Modifier = Modifier
) {
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    val animatedProgress by animateFloatAsState(
        targetValue = if (started) progressValue else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 350, easing = FastOutSlowInEasing),
        label = "progress"
    )

    Surface(
        color = WarmWhite,
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        border = BorderStroke(1.dp, WarmBorder),
        shadowElevation = Dimens.CardElevation,
        modifier = modifier
            .fillMaxWidth()
            .entrance(3)
    ) {
        Column(modifier = Modifier.padding(Dimens.CardPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Progres Fokus Hari Ini",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Espresso,
                        letterSpacing = (-0.1).sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(
                        color = Vanilla,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, WarmBorder)
                    ) {
                        Text(
                            text = "$completedSteps/$totalSteps langkah",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Umber,
                            letterSpacing = 0.1.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Text(
                    text = "${(animatedProgress * 100f).roundToInt()}%",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Marigold,
                    style = LocalTextStyle.current.copy(fontFeatureSettings = "tnum")
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress bar tebal berlatar #FFF8E1 dengan isi #FFAA00 (Marigold)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(TrackBg)
                    .border(1.dp, WarmBorder, RoundedCornerShape(50))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(animatedProgress.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(50))
                        .background(Marigold)
                        .innerHighlight(alpha = 0.4f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Copywriting adaptif
            Text(
                text = motivationText,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Normal,
                color = Umber,
                lineHeight = 20.sp,
                letterSpacing = 0.1.sp
            )
        }
    }
}
