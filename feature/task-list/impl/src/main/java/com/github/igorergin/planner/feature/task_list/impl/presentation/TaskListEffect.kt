package com.github.igorergin.planner.feature.task_list.impl.presentation

import com.github.igorergin.planner.core.mvi.UiEffect

sealed interface TaskListEffect : UiEffect {
    data class NavigateToDetails(val taskId: Int) : TaskListEffect
    data object NavigateToCreateTask : TaskListEffect
}