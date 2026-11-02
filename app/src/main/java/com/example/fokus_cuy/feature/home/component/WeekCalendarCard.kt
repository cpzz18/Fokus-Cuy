package com.example.fokus_cuy.feature.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fokus_cuy.core.designsystem.modifier.bounceClick
import com.example.fokus_cuy.core.designsystem.modifier.entrance
import com.example.fokus_cuy.core.designsystem.modifier.innerHighlight
import com.example.fokus_cuy.core.designsystem.theme.Dimens
import com.example.fokus_cuy.core.designsystem.theme.Espresso
import com.example.fokus_cuy.core.designsystem.theme.Sunburst
import com.example.fokus_cuy.core.designsystem.theme.Umber
import com.example.fokus_cuy.core.designsystem.theme.Vanilla
import com.example.fokus_cuy.core.designsystem.theme.WarmBorder
import com.example.fokus_cuy.core.designsystem.theme.WarmWhite
import com.example.fokus_cuy.core.designsystem.theme.YellowBorder
import com.example.fokus_cuy.feature.home.WeekDay

/**
 * Weekly Calendar Strip (DOKUMENTASI_SLICING_UI.md Section 4.2 point 1).
 * - 7 kolom horizontal (Senin sampai Minggu).
 * - Hari terpilih: #FFD026 (Sunburst) & teks #1E1B18 (Espresso).
 */
@Composable
fun WeekCalendarCard(
    weekDays: List<WeekDay>,
    selectedDayIndex: Int,
    onDaySelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = WarmWhite,
        shape = RoundedCornerShape(Dimens.CardCornerRadius),
        border = BorderStroke(1.dp, WarmBorder),
        shadowElevation = Dimens.CardElevation,
        modifier = modifier
            .fillMaxWidth()
            .entrance(1)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            weekDays.forEachIndexed { index, day ->
                val isSelected = selectedDayIndex == index
                val dayShape = RoundedCornerShape(16.dp)

                val bg by animateColorAsState(
                    targetValue = when {
                        isSelected -> Sunburst
                        day.isToday -> Vanilla
                        else -> Color.Transparent
                    },
                    animationSpec = tween(240),
                    label = "dayBg"
                )
                val edge by animateColorAsState(
                    targetValue = if (isSelected) YellowBorder else Color.Transparent,
                    animationSpec = tween(240),
                    label = "dayEdge"
                )
                val nameColor by animateColorAsState(
                    targetValue = if (isSelected) Espresso else Umber,
                    animationSpec = tween(240),
                    label = "dayName"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .bounceClick(pressedScale = 0.92f, shape = dayShape) {
                            onDaySelected(index)
                        }
                        .background(bg)
                        .border(1.dp, edge, dayShape)
                        .then(
                            if (isSelected) Modifier.innerHighlight(alpha = 0.55f, corner = 16.dp)
                            else Modifier
                        )
                        .padding(horizontal = 10.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = day.dayName,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = nameColor,
                        letterSpacing = 0.2.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = day.dateNum,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Espresso,
                        style = LocalTextStyle.current.copy(fontFeatureSettings = "tnum")
                    )
                }
            }
        }
    }
}
