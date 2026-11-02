package com.example.fokus_cuy.feature.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fokus_cuy.core.designsystem.component.PressableSurface
import com.example.fokus_cuy.core.designsystem.modifier.innerHighlight
import com.example.fokus_cuy.core.designsystem.theme.Espresso
import com.example.fokus_cuy.core.designsystem.theme.Sunburst
import com.example.fokus_cuy.core.designsystem.theme.YellowBorder

/**
 * Floating Action Button (DOKUMENTASI_SLICING_UI.md Section 4.2 point 5).
 * - Kapsul mengambang bertuliskan "+ Tugas Baru" berlatar Sunburst Yellow (#FFD026).
 */
@Composable
fun NewTaskFab(
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    PressableSurface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = Sunburst,
        border = BorderStroke(1.dp, YellowBorder),
        shadowElevation = 4.dp,
        pressedScale = 0.95f,
        modifier = modifier.padding(bottom = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .innerHighlight(alpha = 0.55f)
                .padding(start = 18.dp, end = 22.dp, top = 14.dp, bottom = 14.dp)
        ) {
            Text(
                text = "+",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
                color = Espresso,
                modifier = Modifier.offset(y = (-1).dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tugas Baru",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Espresso,
                letterSpacing = (-0.1).sp
            )
        }
    }
}
