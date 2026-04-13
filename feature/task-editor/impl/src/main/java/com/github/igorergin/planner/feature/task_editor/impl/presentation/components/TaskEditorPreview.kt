package com.github.igorergin.planner.feature.task_editor.impl.presentation.components

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.igorergin.planner.feature.task_editor.impl.presentation.TaskEditorState
import com.github.planner.core.ui.theme.PlannerTheme

@Composable
private fun TaskEditorPreviewContent(
    state: TaskEditorState,
    darkTheme: Boolean = false
) {
    PlannerTheme(darkTheme = darkTheme) {
        TaskEditorContent(
            state = state,
            onIntent = {},
            onBack = {}
        )
    }
}

@Preview(name = "New Task - Light", showBackground = true)
@Composable
fun PreviewNewTaskLight() {
    TaskEditorPreviewContent(
        state = TaskEditorState(
            isEditMode = false,
            title = "",
            description = ""
        )
    )
}

@Preview(name = "Edit Task - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewEditTaskDark() {
    TaskEditorPreviewContent(
        state = TaskEditorState(
            isEditMode = true,
            title = "Важная встреча",
            description = "Обсудить планы на квартал",
            date = System.currentTimeMillis(),
            startHour = 10,
            startMinute = 0,
            endHour = 11,
            endMinute = 30
        ),
        darkTheme = true
    )
}

@Preview(name = "Saving State", showBackground = true)
@Composable
fun PreviewSavingState() {
    TaskEditorPreviewContent(
        state = TaskEditorState(
            isEditMode = false,
            title = "Новая задача",
            isSaving = true
        )
    )
}

@Preview(name = "Landscape", showBackground = true, device = "spec:width=1280dp,height=800dp,orientation=landscape")
@Composable
fun PreviewLandscape() {
    TaskEditorPreviewContent(
        state = TaskEditorState(
            isEditMode = true,
            title = "Задача в ландшафте",
            description = "Длинное описание для проверки прокрутки в горизонтальном режиме экрана. ".repeat(5)
        )
    )
}
