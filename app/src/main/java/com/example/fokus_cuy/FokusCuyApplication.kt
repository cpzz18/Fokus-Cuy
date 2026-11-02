package com.example.fokus_cuy

import android.app.Application
import android.content.Context
import com.example.fokus_cuy.data.local.AppDatabase
import com.example.fokus_cuy.data.repository.TaskRepositoryImpl
import com.example.fokus_cuy.domain.repository.TaskRepository

/**
 * Kontainer Manual Dependency Injection (KISS & zero reflection/overhead).
 */
interface AppContainer {
    val taskRepository: TaskRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    override val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(database.taskDao())
    }
}

class FokusCuyApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}
