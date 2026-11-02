package com.example.fokus_cuy.core.designsystem.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fokus_cuy.core.designsystem.modifier.innerHighlight

/**
 * Badge pill dengan border tipis dan inner highlight di tepi atas.
 */
@Composable
fun PillBadge(
    text: String,
    bg: Color,
    borderColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 11.5.sp,
    verticalPadding: Dp = 4.dp,
    leading: (@Composable () -> Unit)? = null
) {
    Surface(
        color = bg,
        shape = RoundedCornerShape(50),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .innerHighlight(alpha = 0.7f)
                .padding(horizontal = 10.dp, vertical = verticalPadding)
        ) {
            if (leading != null) {
                leading()
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = text,
                fontSize = fontSize,
                fontWeight = FontWeight.Bold,
                color = textColor,
                letterSpacing = 0.1.sp
            )
        }
    }
}
