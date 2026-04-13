package com.github.igorergin.planner.data.repository

import com.github.igorergin.planner.data.local.TaskDao
import com.github.igorergin.planner.data.local.TaskEntity
import com.github.igorergin.planner.data.service.JsonParserService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao,
    private val jsonParserService: JsonParserService
) {
    suspend fun prePopulateDataIfNeeded() {
        val existingTasks = taskDao.getTasksForDay(0, Long.MAX_VALUE).first()
        if (existingTasks.isEmpty()) {
            val jsonTasks = jsonParserService.parseInitialJson()
            val entities = jsonTasks.map { dto ->
                TaskEntity(
                    id = dto.id,
                    name = dto.name,
                    description = dto.description,
                    dateStart = dto.dateStart.toLongOrNull() ?: 0L,
                    dateFinish = dto.dateFinish.toLongOrNull() ?: 0L
                )
            }
            taskDao.insertTasks(entities)
        }
    }

    suspend fun getTaskById(id: Int): TaskEntity? {
        return taskDao.getTaskById(id)
    }

    fun getTasksForDay(dayStart: Long, dayEnd: Long): Flow<List<TaskEntity>> {
        return taskDao.getTasksForDay(dayStart, dayEnd)
    }

    suspend fun saveTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }
}