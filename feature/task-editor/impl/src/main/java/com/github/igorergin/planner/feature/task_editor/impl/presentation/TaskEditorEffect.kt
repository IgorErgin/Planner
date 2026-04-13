package com.github.igorergin.planner.feature.task_editor.impl.presentation

import com.github.igorergin.planner.core.mvi.UiEffect

sealed interface TaskEditorEffect : UiEffect {
    data object NavigateBack : TaskEditorEffect
    data class ShowError(val message: String) : TaskEditorEffect
}