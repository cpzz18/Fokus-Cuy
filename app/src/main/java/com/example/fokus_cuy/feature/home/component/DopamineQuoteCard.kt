package com.example.fokus_cuy.feature.home.component

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fokus_cuy.core.designsystem.component.IconCircleButton
import com.example.fokus_cuy.core.designsystem.icon.RefreshIcon
import com.example.fokus_cuy.core.designsystem.icon.SparkleIcon
import com.example.fokus_cuy.core.designsystem.modifier.entrance
import com.example.fokus_cuy.core.designsystem.modifier.innerHighlight
import com.example.fokus_cuy.core.designsystem.theme.Custard
import com.example.fokus_cuy.core.designsystem.theme.Dimens
import com.example.fokus_cuy.core.designsystem.theme.Espresso
import com.example.fokus_cuy.core.designsystem.theme.Marigold
import com.example.fokus_cuy.core.designsystem.theme.Umber
import com.example.fokus_cuy.core.designsystem.theme.WarmWhite

/**
 * Daily Spark Card / Suntikan Dopamin (DOKUMENTASI_SLICING_UI.md Section 4.2 point 2).
 * - Kontainer berlatar Custard (#FFF6CC) dengan kutipan motivasi rotatif.
 * - Tombol refresh dengan animasi putar halus.
 */
@Composable
fun DopamineQuoteCard(
    quote: String,
    quoteIndex: Int,
    onNextQuote: () -> Unit,
    modifier: Modifier = Modifier
) {
    var spin by remember { mutableFloatStateOf(0f) }
    val spinAnim by animateFloatAsState(
        targetValue = spin,
        animationSpec = tween(550, easing = FastOutSlowInEasing),
        label = "refreshSpin"
    )

    Surface(
        color = Custard,
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        border = BorderStroke(1.dp, Color(0xFFFDE68A)),
        modifier = modifier
            .fillMaxWidth()
            .entrance(2)
    ) {
        Column(
            modifier = Modifier
                .innerHighlight(alpha = 0.55f, corner = Dimens.CardCornerRadius)
                .animateContentSize()
                .padding(Dimens.CardPadding)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    SparkleIcon(size = 16.dp, color = Marigold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "SUNTIKAN DOPAMIN HARI INI",
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Umber,
                        letterSpacing = 0.8.sp
                    )
                }

                IconCircleButton(
                    onClick = {
                        onNextQuote()
                        spin += 360f
                    },
                    size = Dimens.ActionCircleSize,
                    color = WarmWhite
                ) {
                    Box(modifier = Modifier.graphicsLayer { rotationZ = spinAnim }) {
                        RefreshIcon(size = 16.dp, color = Umber)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Crossfade(
                targetState = quoteIndex,
                animationSpec = tween(350),
                label = "quote"
            ) { _ ->
                Text(
                    text = "\u201C$quote\u201D",
                    fontSize = 14.5.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Medium,
                    color = Espresso,
                    lineHeight = 23.sp,
                    letterSpacing = 0.1.sp,
                    modifier = Modifier.padding(end = 16.dp)
                )
            }
        }
    }
}
