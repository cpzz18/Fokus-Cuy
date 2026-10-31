package com.example.fokus_cuy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.*
import kotlinx.coroutines.delay
import com.example.fokus_cuy.data.local.AppDatabase
import com.example.fokus_cuy.data.repository.TaskRepositoryImpl
import com.example.fokus_cuy.ui.tasks.TaskScreen
import com.example.fokus_cuy.ui.tasks.TaskViewModel
import com.example.fokus_cuy.ui.theme.FokusCuyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inisialisasi SQLite Room Database & Repository buatan teman Anda
        val database = AppDatabase.getInstance(applicationContext)
        val repository = TaskRepositoryImpl(database.taskDao())

        // Inisialisasi ViewModel dengan Factory buatan teman Anda
        val viewModel: TaskViewModel by viewModels {
            TaskViewModel.provideFactory(repository)
        }

        setContent {
            FokusCuyTheme {
                AppEntry() // Memanggil UI baru Anda
                // TaskScreen(viewModel = viewModel) // Kode UI lama teman Anda di-comment dulu
            }
        }
    }
}

@Composable
fun AppEntry() {
    var showSplash by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(2000) // Tampilkan splash screen selama 2 detik
        showSplash = false
    }

    if (showSplash) {
        SplashScreen() // Memanggil dari file SplashScreen.kt
    } else {
        HomeScreen()   // Memanggil dari file HomeScreen.kt
    }
}