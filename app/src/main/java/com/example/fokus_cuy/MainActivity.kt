package com.example.fokus_cuy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.fokus_cuy.data.local.AppDatabase
import com.example.fokus_cuy.data.repository.TaskRepositoryImpl
import com.example.fokus_cuy.ui.tasks.TaskScreen
import com.example.fokus_cuy.ui.tasks.TaskViewModel
import com.example.fokus_cuy.ui.theme.FokusCuyTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Inisialisasi SQLite Room Database & Repository
        val database = AppDatabase.getInstance(applicationContext)
        val repository = TaskRepositoryImpl(database.taskDao())

        // Inisialisasi ViewModel dengan Factory
        val viewModel: TaskViewModel by viewModels {
            TaskViewModel.provideFactory(repository)
        }

        setContent {
            FokusCuyTheme {
                TaskScreen(viewModel = viewModel)
            }
        }
    }
}
