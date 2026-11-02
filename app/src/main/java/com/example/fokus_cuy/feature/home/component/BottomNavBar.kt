package com.example.fokus_cuy.feature.home.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fokus_cuy.core.designsystem.modifier.bounceClick
import com.example.fokus_cuy.core.designsystem.modifier.innerHighlight
import com.example.fokus_cuy.core.designsystem.theme.Espresso
import com.example.fokus_cuy.core.designsystem.theme.Sunburst
import com.example.fokus_cuy.core.designsystem.theme.Umber
import com.example.fokus_cuy.core.designsystem.theme.WarmBorder
import com.example.fokus_cuy.core.designsystem.theme.WarmWhite
import com.example.fokus_cuy.core.designsystem.theme.YellowBorder
import com.example.fokus_cuy.navigation.AppDestination

/**
 * Bottom Navigation Bar (DOKUMENTASI_SLICING_UI.md Section 4.1 point 3).
 * - Container berlatar #FFFFFF dengan border #F5EED6.
 * - Tab aktif ditandai latar Sunburst Yellow (#FFD026) dan font bold.
 */
@Composable
fun BottomNavBar(
    selectedDestination: AppDestination,
    onDestinationSelected: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        color = WarmWhite,
        border = BorderStroke(1.dp, WarmBorder),
        shadowElevation = 8.dp,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val itemShape = RoundedCornerShape(20.dp)

            AppDestination.entries.forEach { destination ->
                val isSelected = selectedDestination == destination

                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) Sunburst else Color.Transparent,
                    animationSpec = tween(240),
                    label = "navBg"
                )
                val edgeColor by animateColorAsState(
                    targetValue = if (isSelected) YellowBorder else Color.Transparent,
                    animationSpec = tween(240),
                    label = "navEdge"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) Espresso else Umber,
                    animationSpec = tween(240),
                    label = "navContent"
                )
                val iconScale by animateFloatAsState(
                    targetValue = if (isSelected) 1.08f else 1f,
                    animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
                    label = "navIconScale"
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .bounceClick(pressedScale = 0.92f, shape = itemShape) { onDestinationSelected(destination) }
                        .background(bgColor)
                        .border(1.dp, edgeColor, itemShape)
                        .then(
                            if (isSelected) Modifier.innerHighlight(alpha = 0.55f, corner = 20.dp)
                            else Modifier
                        )
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier.graphicsLayer {
                            scaleX = iconScale
                            scaleY = iconScale
                        }
                    ) {
                        destination.Icon(size = 22.dp, color = contentColor)
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = destination.label,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = contentColor,
                        letterSpacing = 0.1.sp
                    )
                }
            }
        }
    }
}
