package com.example.fokus_cuy.feature.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fokus_cuy.core.designsystem.component.PillBadge
import com.example.fokus_cuy.core.designsystem.modifier.bounceClick
import com.example.fokus_cuy.core.designsystem.modifier.entrance
import com.example.fokus_cuy.core.designsystem.theme.Dimens
import com.example.fokus_cuy.core.designsystem.theme.Espresso
import com.example.fokus_cuy.core.designsystem.theme.Marigold
import com.example.fokus_cuy.core.designsystem.theme.Q1Accent
import com.example.fokus_cuy.core.designsystem.theme.Q1Bg
import com.example.fokus_cuy.core.designsystem.theme.Q1Border
import com.example.fokus_cuy.core.designsystem.theme.Vanilla
import com.example.fokus_cuy.core.designsystem.theme.WarmBorder
import com.example.fokus_cuy.feature.home.component.BottomNavBar
import com.example.fokus_cuy.feature.home.component.DopamineQuoteCard
import com.example.fokus_cuy.feature.home.component.FocusProgressCard
import com.example.fokus_cuy.feature.home.component.HomeHeader
import com.example.fokus_cuy.feature.home.component.MainTaskCard
import com.example.fokus_cuy.feature.home.component.NewTaskFab
import com.example.fokus_cuy.feature.home.component.PlaceholderPage
import com.example.fokus_cuy.feature.home.component.WeekCalendarCard
import com.example.fokus_cuy.navigation.AppDestination

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var currentDestination by remember { mutableStateOf(AppDestination.TODAY) }

    Scaffold(
        modifier = modifier,
        containerColor = Vanilla, // Latar belakang kanvas utama #FFFDF2 (Section 2.1)
        bottomBar = {
            BottomNavBar(
                selectedDestination = currentDestination,
                onDestinationSelected = { currentDestination = it }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentDestination == AppDestination.TODAY,
                enter = fadeIn(tween(220)) + scaleIn(initialScale = 0.85f, animationSpec = tween(220)),
                exit = fadeOut(tween(150)) + scaleOut(targetScale = 0.85f, animationSpec = tween(150))
            ) {
                NewTaskFab(onClick = { /* Buka form task baru */ })
            }
        }
    ) { innerPadding ->
        Crossfade(
            targetState = currentDestination,
            animationSpec = tween(220),
            label = "tabContent"
        ) { destination ->
            when (destination) {
                AppDestination.TODAY -> TodayContent(
                    uiState = uiState,
                    onDaySelected = viewModel::selectDay,
                    onNextQuote = viewModel::nextQuote,
                    onToggleMainTask = viewModel::toggleMainTask,
                    modifier = Modifier.padding(innerPadding)
                )
                AppDestination.MATRIX -> PlaceholderPage(
                    title = "Halaman Matriks",
                    modifier = Modifier.padding(innerPadding)
                )
                AppDestination.FOCUS -> PlaceholderPage(
                    title = "Halaman Fokus",
                    modifier = Modifier.padding(innerPadding)
                )
                AppDestination.STATS -> PlaceholderPage(
                    title = "Halaman Statistik",
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@Composable
fun TodayContent(
    uiState: HomeUiState,
    onDaySelected: (Int) -> Unit,
    onNextQuote: () -> Unit,
    onToggleMainTask: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // 1. Header & Sapaan Dinamis
        HomeHeader(
            greetingTitle = uiState.greetingTitle,
            streakDays = uiState.streakDays
        )

        HorizontalDivider(
            color = WarmBorder,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.ScreenHorizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // 2. Kalender Mingguan
            WeekCalendarCard(
                weekDays = uiState.weekDays,
                selectedDayIndex = uiState.selectedDayIndex,
                onDaySelected = onDaySelected
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Suntikan Dopamin
            DopamineQuoteCard(
                quote = uiState.currentQuote,
                quoteIndex = uiState.quoteIndex,
                onNextQuote = onNextQuote
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Progres Fokus Hari Ini (Momentum Bar Adaptif)
            FocusProgressCard(
                progressValue = uiState.progress,
                completedSteps = uiState.completedSteps,
                totalSteps = uiState.totalSteps,
                motivationText = uiState.progressMotivationText
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 5. Section Header Fokus Utama
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
                        fontWeight = FontWeight.Bold,
                        color = Espresso,
                        letterSpacing = (-0.3).sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    PillBadge(
                        text = "Prioritas Q1",
                        bg = Q1Bg,
                        borderColor = Q1Border,
                        textColor = Q1Accent
                    )
                }

                Text(
                    text = "+ Tambah",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Marigold,
                    letterSpacing = 0.1.sp,
                    modifier = Modifier
                        .offset(x = 8.dp)
                        .bounceClick(pressedScale = 0.92f, shape = RoundedCornerShape(50)) { }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 6. Kartu Tugas Utama (Kuadran 1)
            MainTaskCard(
                isChecked = uiState.isMainTaskChecked,
                title = uiState.mainTaskTitle,
                nextStep = uiState.mainTaskNextStep,
                onToggleCheck = onToggleMainTask,
                hasTask = uiState.hasMainTask,
                completedSteps = uiState.mainTaskCompletedSteps,
                totalSteps = uiState.mainTaskTotalSteps,
                pomodoroCount = uiState.mainTaskPomodoroCount,
                quadrant = uiState.mainTaskQuadrant,
                source = uiState.mainTaskSource
            )

            Spacer(modifier = Modifier.height(Dimens.ContentBottomPadding))
        }
    }
}
