package com.github.igorergin.planner.feature.task_editor.impl.domain.model

data class EditorTaskModel(
    val id: Int,
    val title: String,
    val description: String,
    val dateStart: Long,
    val dateFinish: Long
)