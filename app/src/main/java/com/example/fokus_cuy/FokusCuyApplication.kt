package com.example.fokus_cuy

import android.app.Application
import android.content.Context
import android.util.Log
import com.example.fokus_cuy.BuildConfig
import com.example.fokus_cuy.data.local.AppDatabase
import com.example.fokus_cuy.data.repository.DistractionRepositoryImpl
import com.example.fokus_cuy.data.repository.FocusSessionRepositoryImpl
import com.example.fokus_cuy.data.repository.TaskRepositoryImpl
import com.example.fokus_cuy.data.repository.UserRepositoryImpl
import com.example.fokus_cuy.data.seed.DebugDatabaseSeeder
import com.example.fokus_cuy.domain.repository.DistractionRepository
import com.example.fokus_cuy.domain.repository.FocusSessionRepository
import com.example.fokus_cuy.domain.repository.TaskRepository
import com.example.fokus_cuy.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.time.Clock

/**
 * Kontainer Manual Dependency Injection (KISS & zero reflection/overhead).
 */
interface AppContainer {
    val clock: Clock
    val taskRepository: TaskRepository
    val focusSessionRepository: FocusSessionRepository
    val distractionRepository: DistractionRepository
    val userRepository: UserRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    override val clock: Clock = Clock.systemDefaultZone()

    private val database: AppDatabase by lazy {
        AppDatabase.getInstance(context)
    }

    override val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(database, clock)
    }

    override val focusSessionRepository: FocusSessionRepository by lazy {
        FocusSessionRepositoryImpl(database, clock)
    }

    override val distractionRepository: DistractionRepository by lazy {
        DistractionRepositoryImpl(database, clock)
    }

    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(database, clock)
    }
}

class FokusCuyApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        val appContainer = DefaultAppContainer(this)
        container = appContainer

        // Bootstrap data awal (guest user & debug seed) dengan penanganan error aman
        applicationScope.launch {
            runCatching {
                val user = appContainer.userRepository.ensureLocalGuestUser()
                if (BuildConfig.DEBUG) {
                    DebugDatabaseSeeder.seedIfEmpty(
                        userId = user.id,
                        taskRepository = appContainer.taskRepository,
                        focusSessionRepository = appContainer.focusSessionRepository,
                        clock = appContainer.clock
                    )
                }
            }.onFailure { error ->
                Log.e("FokusCuyApplication", "Gagal melakukan bootstrap data awal database", error)
            }
        }
    }
}
