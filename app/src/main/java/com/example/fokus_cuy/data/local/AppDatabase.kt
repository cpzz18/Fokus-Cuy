package com.example.fokus_cuy.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fokus_cuy.data.local.dao.DistractionNoteDao
import com.example.fokus_cuy.data.local.dao.FocusSessionDao
import com.example.fokus_cuy.data.local.dao.SubtaskDao
import com.example.fokus_cuy.data.local.dao.TaskDao
import com.example.fokus_cuy.data.local.dao.UserDao
import com.example.fokus_cuy.data.local.entity.DistractionNoteEntity
import com.example.fokus_cuy.data.local.entity.FocusSessionEntity
import com.example.fokus_cuy.data.local.entity.SubtaskEntity
import com.example.fokus_cuy.data.local.entity.TaskEntity
import com.example.fokus_cuy.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        TaskEntity::class,
        SubtaskEntity::class,
        FocusSessionEntity::class,
        DistractionNoteEntity::class
    ],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun subtaskDao(): SubtaskDao
    abstract fun focusSessionDao(): FocusSessionDao
    abstract fun distractionNoteDao(): DistractionNoteDao
    abstract fun userDao(): UserDao

    companion object {
        private const val DATABASE_NAME = "fokus_cuy.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    // Hanya destruktif jika migrasi dari v1 (scaffold lama).
                    // Versi selanjutnya wajib menyediakan Migration eksplisit agar data pengguna aman.
                    .fallbackToDestructiveMigrationFrom(dropAllTables = true, 1)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
