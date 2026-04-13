package com.github.igorergin.planner.feature.task_editor.impl.presentation

import com.github.igorergin.planner.core.mvi.UiState

data class TaskEditorState(
    val taskId: Int? = null,
    val title: String = "",
    val description: String = "",
    val date: Long = 0L,
    val startHour: Int = 12,
    val startMinute: Int = 0,
    val endHour: Int = 13,
    val endMinute: Int = 0,
    val isSaving: Boolean = false,
    val isEditMode: Boolean = false
) : UiState