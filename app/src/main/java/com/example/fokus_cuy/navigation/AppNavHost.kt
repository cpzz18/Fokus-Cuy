package com.example.fokus_cuy.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fokus_cuy.feature.home.HomeScreen
import com.example.fokus_cuy.feature.splash.SplashScreen
import com.example.fokus_cuy.feature.splash.SplashViewModel

/**
 * Root host navigasi aplikasi (mengatur alur dari Splash ke Home secara halus).
 */
@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    splashViewModel: SplashViewModel = viewModel()
) {
    val isSplashFinished by splashViewModel.isFinished.collectAsState()

    Crossfade(
        targetState = isSplashFinished,
        animationSpec = tween(durationMillis = 400),
        label = "appNavigation"
    ) { finished ->
        if (!finished) {
            SplashScreen(modifier = modifier)
        } else {
            HomeScreen(modifier = modifier)
        }
    }
}
