package com.github.igorergin.planner.feature.task_list.impl.domain.usecase

import com.github.igorergin.planner.feature.task_list.api.domain.model.TaskModel
import com.github.igorergin.planner.data.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetTasksForDayUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(selectedDate: Long): Flow<List<TaskModel>> {
        val dayStart = selectedDate
        val dayEnd = dayStart + 86400000L

        return repository.getTasksForDay(dayStart, dayEnd).map { entities ->
            entities.map { entity ->
                TaskModel(
                    id = entity.id,
                    title = entity.name,
                    description = entity.description,
                    startTimestamp = entity.dateStart,
                    finishTimestamp = entity.dateFinish,
                    isCompleted = false
                )
            }
        }
    }
}