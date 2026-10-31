package com.example.fokus_cuy

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

@Composable
fun HomeScreen() {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        containerColor = CreamBg,
        bottomBar = {
            BottomMenu(
                selectedIndex = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedTab == 0,
                enter = fadeIn(tween(220)) + scaleIn(initialScale = 0.85f, animationSpec = tween(220)),
                exit = fadeOut(tween(150)) + scaleOut(targetScale = 0.85f, animationSpec = tween(150))
            ) {
                NewTaskFab()
            }
        }
    ) { innerPadding ->
        // Menampilkan isi halaman sesuai tab yang dipilih (transisi crossfade halus)
        Crossfade(
            targetState = selectedTab,
            animationSpec = tween(220),
            label = "tabContent"
        ) { tab ->
            when (tab) {
                0 -> HariIniContent(modifier = Modifier.padding(innerPadding))
                1 -> PlaceholderPage(title = "Halaman Matriks", modifier = Modifier.padding(innerPadding))
                2 -> PlaceholderPage(title = "Halaman Fokus", modifier = Modifier.padding(innerPadding))
                3 -> PlaceholderPage(title = "Halaman Statistik", modifier = Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
private fun NewTaskFab() {
    PressableSurface(
        onClick = { },
        shape = RoundedCornerShape(50),
        color = YellowPrimary,
        border = BorderStroke(1.dp, YellowBorder),
        shadowElevation = 4.dp,
        pressedScale = 0.95f,
        modifier = Modifier.padding(bottom = 4.dp)
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
                color = DarkText,
                modifier = Modifier.offset(y = (-1).dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Tugas Baru",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                letterSpacing = (-0.1).sp
            )
        }
    }
}

/** Badge pill dengan border tipis + highlight tepi atas agar terasa berdimensi. */
@Composable
private fun PillBadge(
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

@Composable
fun HariIniContent(modifier: Modifier = Modifier) {
    // Progress bar & persentase naik pelan saat layar pertama muncul
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    val progress by animateFloatAsState(
        targetValue = if (started) 0.38f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 350, easing = FastOutSlowInEasing),
        label = "progress"
    )

    // Api streak bernapas pelan
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
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 1. HEADER & SAMBUTAN
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .entrance(0)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PressableSurface(
                    onClick = { },
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
                            color = YellowPrimary,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PressableSurface(
                        onClick = { },
                        color = StreakBg,
                        shape = RoundedCornerShape(50),
                        border = BorderStroke(1.dp, StreakBorder),
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
                                FlameIcon(size = 15.dp)
                            }
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "5 Hari",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkText,
                                letterSpacing = 0.1.sp
                            )
                        }
                    }

                    PressableSurface(
                        onClick = { },
                        color = IconButtonBg,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, LightBorder),
                        shadowElevation = 1.dp,
                        pressedScale = 0.88f,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CompassIcon(size = 18.dp, color = OrangeAccent)
                        }
                    }

                    PressableSurface(
                        onClick = { },
                        color = IconButtonBg,
                        shape = CircleShape,
                        border = BorderStroke(1.dp, LightBorder),
                        shadowElevation = 1.dp,
                        pressedScale = 0.88f,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            SpeakerIcon(size = 17.dp, color = GrayText)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Halo, Dimas! Yuk, cicil dikit lagi sebelum santai.",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = DarkText,
                letterSpacing = (-0.4).sp,
                lineHeight = 23.sp
            )

            Spacer(modifier = Modifier.height(5.dp))

            Text(
                text = "Yuk, cicil langkah kecilmu hari ini agar target tercapai tanpa beban.",
                fontSize = 13.sp,
                color = GrayText,
                lineHeight = 19.sp,
                letterSpacing = 0.1.sp
            )
        }

        HorizontalDivider(
            color = LightBorder,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 2. KARTU KALENDER MINGGUAN
            var selectedDay by remember { mutableIntStateOf(6) }
            val days = listOf(
                "Sen" to "14",
                "Sel" to "15",
                "Rab" to "16",
                "Kam" to "17",
                "Jum" to "18",
                "Sab" to "19",
                "Min" to "20"
            )

            Surface(
                color = CardWhite,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, LightBorder),
                shadowElevation = 1.dp,
                modifier = Modifier
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
                    days.forEachIndexed { index, (dayName, dateNum) ->
                        val isSelected = selectedDay == index
                        val dayShape = RoundedCornerShape(16.dp)

                        val bg by animateColorAsState(
                            targetValue = when {
                                isSelected -> YellowPrimary
                                index == 1 -> CreamBg
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
                            targetValue = if (isSelected) DarkText else GrayText,
                            animationSpec = tween(240),
                            label = "dayName"
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .bounceClick(pressedScale = 0.92f, shape = dayShape) {
                                    selectedDay = index
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
                                text = dayName,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                                color = nameColor,
                                letterSpacing = 0.2.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = dateNum,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = DarkText,
                                style = LocalTextStyle.current.copy(fontFeatureSettings = "tnum")
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. KARTU SUNTIKAN DOPAMIN HARI INI
            val quotes = remember {
                listOf(
                    "Tugas tebal itu cuma kumpulan tugas-tugas kecil yang lagi ngumpul. Pecah dulu, sikat satu-satu!",
                    "Gak perlu sempurna, yang penting mulai. Lima menit pertama itu yang paling berat.",
                    "Satu langkah kecil hari ini lebih berarti daripada rencana besar yang cuma dipikirin.",
                    "Istirahat itu bagian dari proses, bukan hadiah setelah semuanya selesai.",
                    "Progres kecil yang konsisten ngalahin semangat besar yang cuma sebentar."
                )
            }
            var quoteIndex by remember { mutableIntStateOf(0) }
            var spin by remember { mutableFloatStateOf(0f) }
            val spinAnim by animateFloatAsState(
                targetValue = spin,
                animationSpec = tween(550, easing = FastOutSlowInEasing),
                label = "refreshSpin"
            )

            Surface(
                color = DopamineCardBg,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, DopamineBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(2)
            ) {
                Column(
                    modifier = Modifier
                        .innerHighlight(alpha = 0.55f, corner = 24.dp)
                        .animateContentSize()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            SparkleIcon(size = 16.dp, color = OrangeAccent)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SUNTIKAN DOPAMIN HARI INI",
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = AmberText,
                                letterSpacing = 0.8.sp
                            )
                        }

                        PressableSurface(
                            onClick = {
                                quoteIndex = (quoteIndex + 1) % quotes.size
                                spin += 360f
                            },
                            color = CardWhite,
                            shape = CircleShape,
                            border = BorderStroke(1.dp, LightBorder),
                            shadowElevation = 1.dp,
                            pressedScale = 0.88f,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Box(modifier = Modifier.graphicsLayer { rotationZ = spinAnim }) {
                                    RefreshIcon(size = 16.dp, color = GrayText)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Crossfade(
                        targetState = quoteIndex,
                        animationSpec = tween(350),
                        label = "quote"
                    ) { idx ->
                        Text(
                            text = "\u201C${quotes[idx]}\u201D",
                            fontSize = 14.5.sp,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium,
                            color = DarkText,
                            lineHeight = 23.sp,
                            letterSpacing = 0.1.sp,
                            modifier = Modifier.padding(end = 16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. KARTU PROGRES FOKUS HARI INI
            Surface(
                color = CardWhite,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, LightBorder),
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(3)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
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
                                color = DarkText,
                                letterSpacing = (-0.1).sp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = CreamBg,
                                shape = RoundedCornerShape(50),
                                border = BorderStroke(1.dp, LightBorder)
                            ) {
                                Text(
                                    text = "3/8 langkah",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = GrayText,
                                    letterSpacing = 0.1.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Text(
                            text = "${(progress * 100f).roundToInt()}%",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = OrangeAccent,
                            style = LocalTextStyle.current.copy(fontFeatureSettings = "tnum")
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(12.dp)
                            .clip(RoundedCornerShape(50))
                            .background(TrackBg)
                            .border(1.dp, TrackBorder, RoundedCornerShape(50))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(progress)
                                .fillMaxHeight()
                                .clip(RoundedCornerShape(50))
                                .background(OrangeAccent)
                                .innerHighlight(alpha = 0.4f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Mantap! Udah 38% jalan menuju bebas tugas hari ini.",
                        fontSize = 13.5.sp,
                        color = GrayText,
                        lineHeight = 20.sp,
                        letterSpacing = 0.1.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(22.dp))

            // 5. JUDUL SECTION "Fokus Utama Hari Ini"
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(4),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Fokus Utama Hari Ini",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DarkText,
                        letterSpacing = (-0.3).sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    PillBadge(
                        text = "Prioritas Q1",
                        bg = RedPillBg,
                        borderColor = RedPillBorder,
                        textColor = RedPillText
                    )
                }

                // Area tap diperluas 8dp tiap sisi, lalu digeser agar teks tetap rata kanan secara optis
                Text(
                    text = "+ Tambah",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = OrangeAccent,
                    letterSpacing = 0.1.sp,
                    modifier = Modifier
                        .offset(x = 8.dp)
                        .bounceClick(pressedScale = 0.92f, shape = RoundedCornerShape(50)) { }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 6. KARTU TUGAS UTAMA
            var checked by remember { mutableStateOf(false) }
            val checkFill by animateColorAsState(
                targetValue = if (checked) OrangeAccent else Color.Transparent,
                animationSpec = tween(220),
                label = "checkFill"
            )
            val checkScale by animateFloatAsState(
                targetValue = if (checked) 1f else 0f,
                animationSpec = spring(dampingRatio = 0.5f, stiffness = Spring.StiffnessMedium),
                label = "checkScale"
            )
            val titleColor by animateColorAsState(
                targetValue = if (checked) GrayText else DarkText,
                animationSpec = tween(220),
                label = "titleColor"
            )

            PressableSurface(
                onClick = { },
                color = CardWhite,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, LightBorder),
                shadowElevation = 2.dp,
                pressedScale = 0.98f,
                modifier = Modifier
                    .fillMaxWidth()
                    .entrance(5)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .bounceClick(pressedScale = 0.85f, shape = CircleShape) {
                                    checked = !checked
                                }
                                .background(checkFill)
                                .border(1.8.dp, OrangeAccent, CircleShape),
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
                            PillBadge(
                                text = "Q1: Sikat Sekarang!",
                                bg = RedPillBg,
                                borderColor = RedPillBorder,
                                textColor = RedPillText
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            PillBadge(
                                text = "AI PDF",
                                bg = AiPillBg,
                                borderColor = DopamineBorder,
                                textColor = OrangeAccent,
                                fontSize = 11.sp,
                                verticalPadding = 3.dp,
                                leading = { SparkleIcon(size = 12.dp, color = OrangeAccent) }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Laporan Akhir Praktikum\nBasis Data",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = titleColor,
                                lineHeight = 23.sp,
                                letterSpacing = (-0.3).sp,
                                textDecoration = if (checked) TextDecoration.LineThrough else TextDecoration.None
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                LayersIcon(size = 15.dp, color = OrangeAccent)
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "2/5 langkah",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = GrayText,
                                    letterSpacing = 0.1.sp
                                )

                                Spacer(modifier = Modifier.width(14.dp))

                                ClockIcon(size = 15.dp, color = GrayText)
                                Spacer(modifier = Modifier.width(5.dp))
                                Text(
                                    text = "7 sesi",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = GrayText,
                                    letterSpacing = 0.1.sp
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 40.dp)
                        ) {
                            PressableSurface(
                                onClick = { },
                                color = YellowPrimary,
                                shape = CircleShape,
                                border = BorderStroke(1.dp, YellowBorder),
                                shadowElevation = 2.dp,
                                pressedScale = 0.88f,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .innerHighlight(alpha = 0.55f),
                                    contentAlignment = Alignment.Center
                                ) {
                                    PlayTriangleIcon(size = 16.dp, color = DarkText)
                                }
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            ChevronRightIcon(size = 16.dp, color = GrayText)
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    HorizontalDivider(color = LightBorder, thickness = 1.dp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = DarkText)) {
                                append("Langkah berikutnya: ")
                            }
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = GrayText)) {
                                append("Tulis query SQL")
                            }
                        },
                        fontSize = 13.sp,
                        letterSpacing = 0.1.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.height(90.dp))
        }
    }
}

@Composable
fun PlaceholderPage(title: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = DarkText,
            letterSpacing = (-0.2).sp
        )
    }
}

@Composable
fun BottomMenu(
    selectedIndex: Int,
    onTabSelected: (Int) -> Unit
) {
    Surface(
        color = CardWhite,
        border = BorderStroke(1.dp, LightBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf("Hari Ini", "Matriks", "Fokus", "Statistik")
            val itemShape = RoundedCornerShape(20.dp)

            items.forEachIndexed { index, label ->
                val isSelected = selectedIndex == index

                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) YellowPrimary else Color.Transparent,
                    animationSpec = tween(240),
                    label = "navBg"
                )
                val edgeColor by animateColorAsState(
                    targetValue = if (isSelected) YellowBorder else Color.Transparent,
                    animationSpec = tween(240),
                    label = "navEdge"
                )
                val contentColor by animateColorAsState(
                    targetValue = if (isSelected) DarkText else GrayText,
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
                        .bounceClick(pressedScale = 0.92f, shape = itemShape) { onTabSelected(index) }
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
                        when (index) {
                            0 -> SunIcon(size = 22.dp, color = contentColor)
                            1 -> GridIcon(size = 22.dp, color = contentColor)
                            2 -> StopwatchIcon(size = 22.dp, color = contentColor)
                            3 -> ChartIcon(size = 22.dp, color = contentColor)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = label,
                        fontSize = 11.5.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = contentColor,
                        letterSpacing = 0.1.sp
                    )
                }
            }
        }
    }
}