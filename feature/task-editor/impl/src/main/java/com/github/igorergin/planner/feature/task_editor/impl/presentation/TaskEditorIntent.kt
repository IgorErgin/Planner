package com.github.igorergin.planner.feature.task_editor.impl.presentation

import com.github.igorergin.planner.core.mvi.UiIntent

sealed interface TaskEditorIntent : UiIntent {
    data class OnTitleChanged(val value: String) : TaskEditorIntent
    data class OnDescriptionChanged(val value: String) : TaskEditorIntent
    data class OnDateSelected(val timestamp: Long) : TaskEditorIntent
    data class OnStartTimeSelected(val hour: Int, val minute: Int) : TaskEditorIntent
    data class OnEndTimeSelected(val hour: Int, val minute: Int) : TaskEditorIntent
    data object OnSaveClicked : TaskEditorIntent
}
