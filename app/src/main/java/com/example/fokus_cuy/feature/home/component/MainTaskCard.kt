package com.example.fokus_cuy.feature.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fokus_cuy.core.designsystem.component.PillBadge
import com.example.fokus_cuy.core.designsystem.component.PressableSurface
import com.example.fokus_cuy.core.designsystem.icon.CheckIcon
import com.example.fokus_cuy.core.designsystem.icon.ChevronRightIcon
import com.example.fokus_cuy.core.designsystem.icon.ClockIcon
import com.example.fokus_cuy.core.designsystem.icon.LayersIcon
import com.example.fokus_cuy.core.designsystem.icon.PlayTriangleIcon
import com.example.fokus_cuy.core.designsystem.icon.SparkleIcon
import com.example.fokus_cuy.core.designsystem.modifier.bounceClick
import com.example.fokus_cuy.core.designsystem.modifier.entrance
import com.example.fokus_cuy.core.designsystem.modifier.innerHighlight
import com.example.fokus_cuy.core.designsystem.theme.Custard
import com.example.fokus_cuy.core.designsystem.theme.Dimens
import com.example.fokus_cuy.core.designsystem.theme.Espresso
import com.example.fokus_cuy.core.designsystem.theme.Marigold
import com.example.fokus_cuy.core.designsystem.theme.Q1Accent
import com.example.fokus_cuy.core.designsystem.theme.Q1Bg
import com.example.fokus_cuy.core.designsystem.theme.Q1Border
import com.example.fokus_cuy.core.designsystem.theme.Q2Accent
import com.example.fokus_cuy.core.designsystem.theme.Q2Bg
import com.example.fokus_cuy.core.designsystem.theme.Q2Border
import com.example.fokus_cuy.core.designsystem.theme.Q3Accent
import com.example.fokus_cuy.core.designsystem.theme.Q3Bg
import com.example.fokus_cuy.core.designsystem.theme.Q3Border
import com.example.fokus_cuy.core.designsystem.theme.Q4Accent
import com.example.fokus_cuy.core.designsystem.theme.Q4Bg
import com.example.fokus_cuy.core.designsystem.theme.Q4Border
import com.example.fokus_cuy.core.designsystem.theme.Sunburst
import com.example.fokus_cuy.core.designsystem.theme.Umber
import com.example.fokus_cuy.core.designsystem.theme.WarmBorder
import com.example.fokus_cuy.core.designsystem.theme.WarmWhite
import com.example.fokus_cuy.core.designsystem.theme.YellowBorder
import com.example.fokus_cuy.domain.model.Quadrant
import com.example.fokus_cuy.domain.model.TaskSource

/**
 * Kartu Tugas Utama (DOKUMENTASI_SLICING_UI.md Section 4.2 point 4).
 * - Kartu bersudut 20px-22px berlatar #FFFFFF dan border #F5EED6.
 * - Lencana Kuadran adaptif (Q1..Q4).
 * - Lencana AI PDF kondisional (hanya jika sumber tugas dari AI_PDF).
 * - Checkbox bulat kustom dan tombol Play (#FFD026).
 */
@Composable
fun MainTaskCard(
    isChecked: Boolean,
    title: String,
    nextStep: String,
    onToggleCheck: () -> Unit,
    onClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    hasTask: Boolean = true,
    completedSteps: Int = 0,
    totalSteps: Int = 0,
    pomodoroCount: Int = 0,
    quadrant: Quadrant = Quadrant.Q1,
    source: TaskSource = TaskSource.MANUAL
) {
    val checkFill by animateColorAsState(
        targetValue = if (isChecked) Marigold else Color.Transparent,
        animationSpec = tween(220),
        label = "checkFill"
    )
    val checkScale by animateFloatAsState(
        targetValue = if (isChecked) 1f else 0f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
        label = "checkScale"
    )
    val titleColor by animateColorAsState(
        targetValue = if (isChecked) Umber else Espresso,
        animationSpec = tween(220),
        label = "titleColor"
    )

    val (quadrantText, qBg, qBorder, qAccent) = when (quadrant) {
        Quadrant.Q1 -> QuadTheme("Q1: Sikat Sekarang!", Q1Bg, Q1Border, Q1Accent)
        Quadrant.Q2 -> QuadTheme("Q2: Cicil Terjadwal", Q2Bg, Q2Border, Q2Accent)
        Quadrant.Q3 -> QuadTheme("Q3: Selesaikan Cepat", Q3Bg, Q3Border, Q3Accent)
        Quadrant.Q4 -> QuadTheme("Q4: Simpan di Kulkas", Q4Bg, Q4Border, Q4Accent)
    }

    PressableSurface(
        onClick = if (hasTask) onClick else ({}),
        color = WarmWhite,
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        border = BorderStroke(1.dp, WarmBorder),
        shadowElevation = 2.dp,
        pressedScale = if (hasTask) 0.98f else 1f,
        modifier = modifier
            .fillMaxWidth()
            .entrance(5)
    ) {
        Column(modifier = Modifier.padding(Dimens.CardPadding)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                // Checkbox bulat kustom
                Box(
                    modifier = Modifier
                        .size(Dimens.CheckboxSize)
                        .then(
                            if (hasTask) {
                                Modifier.bounceClick(pressedScale = 0.85f, shape = CircleShape) {
                                    onToggleCheck()
                                }
                            } else Modifier
                        )
                        .background(checkFill)
                        .border(1.8.dp, if (hasTask) Marigold else WarmBorder, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.graphicsLayer {
                            scaleX = checkScale
                            scaleY = checkScale
                            alpha = checkScale.coerceIn(0f, 1f)
                        }
                    ) {
                        CheckIcon(size = 16.dp, color = Color.White)
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    // Badge Kuadran Adaptif
                    if (hasTask) {
                        PillBadge(
                            text = quadrantText,
                            bg = qBg,
                            borderColor = qBorder,
                            textColor = qAccent
                        )
                    }

                    // Badge AI PDF (hanya tampil bila sumbernya AI_PDF)
                    if (hasTask && source == TaskSource.AI_PDF) {
                        Spacer(modifier = Modifier.height(6.dp))
                        PillBadge(
                            text = "AI PDF",
                            bg = Custard,
                            borderColor = Color(0xFFFDE68A),
                            textColor = Marigold,
                            fontSize = 11.sp,
                            verticalPadding = 3.dp,
                            leading = { SparkleIcon(size = 12.dp, color = Marigold) }
                        )
                    }

                    Spacer(modifier = Modifier.height(if (hasTask) 10.dp else 4.dp))

                    Text(
                        text = title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = titleColor,
                        lineHeight = 22.sp,
                        letterSpacing = (-0.2).sp,
                        textDecoration = if (isChecked) TextDecoration.LineThrough else TextDecoration.None
                    )

                    if (hasTask) {
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            LayersIcon(size = 15.dp, color = Marigold)
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "$completedSteps/$totalSteps langkah",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Umber,
                                letterSpacing = 0.1.sp
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            ClockIcon(size = 15.dp, color = Umber)
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "$pomodoroCount sesi",
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Medium,
                                color = Umber,
                                letterSpacing = 0.1.sp
                            )
                        }
                    }
                }

                if (hasTask) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 40.dp)
                    ) {
                        // Tombol Play Kuning Sunburst (#FFD026)
                        PressableSurface(
                            onClick = onPlayClick,
                            color = Sunburst,
                            shape = CircleShape,
                            border = BorderStroke(1.dp, YellowBorder),
                            shadowElevation = 2.dp,
                            pressedScale = 0.88f,
                            modifier = Modifier.size(Dimens.PlayButtonSize)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .innerHighlight(alpha = 0.55f),
                                contentAlignment = Alignment.Center
                            ) {
                                PlayTriangleIcon(size = 16.dp, color = Espresso)
                            }
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        ChevronRightIcon(size = 16.dp, color = Umber)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            HorizontalDivider(color = WarmBorder, thickness = 1.dp)

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Espresso)) {
                        append("Langkah berikutnya: ")
                    }
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Umber)) {
                        append(nextStep)
                    }
                },
                fontSize = 13.sp,
                letterSpacing = 0.1.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

private data class QuadTheme(
    val label: String,
    val bg: Color,
    val border: Color,
    val accent: Color
)
