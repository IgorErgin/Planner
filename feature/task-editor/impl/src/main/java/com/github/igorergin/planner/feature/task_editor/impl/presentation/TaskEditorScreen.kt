package com.github.igorergin.planner.feature.task_editor.impl.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.igorergin.planner.feature.task_editor.impl.presentation.components.TaskEditorContent

@Composable
fun TaskEditorScreen(
    viewModel: TaskEditorViewModel,
    onBack: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            if (effect is TaskEditorEffect.NavigateBack) {
                onBack()
            }
        }
    }

    TaskEditorContent(
        state = state,
        onIntent = viewModel::onIntent,
        onBack = onBack
    )
}
