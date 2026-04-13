package com.github.igorergin.planner.feature.task_list.impl.presentation

import com.github.igorergin.planner.core.mvi.UiState
import com.github.igorergin.planner.feature.task_list.api.domain.model.TaskModel

data class TaskListState(
    val items: List<TaskModel> = emptyList(),
    val selectedDate: Long = System.currentTimeMillis(),
    val isLoading: Boolean = false,
    val error: String? = null
) : UiState