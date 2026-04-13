package com.github.igorergin.planner.feature.task_editor.api

import kotlinx.serialization.Serializable

@Serializable
data class TaskEditorRoute(
    val taskId: Int? = null
)