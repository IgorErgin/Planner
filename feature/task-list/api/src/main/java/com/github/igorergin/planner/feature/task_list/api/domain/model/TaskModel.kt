package com.github.igorergin.planner.feature.task_list.api.domain.model

data class TaskModel(
    val id: Int,
    val title: String,
    val description: String,
    val startTimestamp: Long,
    val finishTimestamp: Long,
    val isCompleted: Boolean
)