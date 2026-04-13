package com.github.igorergin.planner.feature.task_editor.impl.domain.usecase

import com.github.igorergin.planner.feature.task_editor.impl.domain.model.EditorTaskModel
import com.github.igorergin.planner.data.repository.TaskRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: Int): EditorTaskModel? {
        val entity = repository.getTaskById(taskId) ?: return null
        return EditorTaskModel(
            id = entity.id,
            title = entity.name,
            description = entity.description,
            dateStart = entity.dateStart,
            dateFinish = entity.dateFinish
        )
    }
}