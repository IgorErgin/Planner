package com.github.igorergin.planner.di

import android.content.Context
import androidx.room.Room
import com.github.igorergin.planner.data.local.AppDatabase
import com.github.igorergin.planner.data.local.TaskDao
import com.github.igorergin.planner.data.model.TaskDto
import com.github.igorergin.planner.data.repository.TaskRepository
import com.github.igorergin.planner.data.service.JsonParserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "planner_db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideTaskDao(db: AppDatabase): TaskDao = db.taskDao()

    @Provides
    @Singleton
    fun provideJsonParserService(
        @ApplicationContext context: Context,
        json: Json
    ): JsonParserService = object : JsonParserService {

        override suspend fun parseInitialJson(): List<TaskDto> {
            return try {
                context.assets.open("tasks.json")
                    .bufferedReader()
                    .use { it.readText() }
                    .let { json.decodeFromString<List<TaskDto>>(it) }
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        dao: TaskDao,
        jsonService: JsonParserService
    ): TaskRepository = TaskRepository(dao, jsonService)
}