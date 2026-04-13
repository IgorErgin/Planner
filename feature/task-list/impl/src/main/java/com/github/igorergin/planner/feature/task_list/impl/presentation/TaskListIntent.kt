package com.github.igorergin.planner.feature.task_list.impl.presentation

import com.github.igorergin.planner.core.mvi.UiIntent

sealed interface TaskListIntent : UiIntent {
    data object LoadTasks : TaskListIntent
    data class OnDateSelected(val timestamp: Long) : TaskListIntent
    data class OnTaskClick(val id: Int) : TaskListIntent
    data object OnAddTaskClick : TaskListIntent
}