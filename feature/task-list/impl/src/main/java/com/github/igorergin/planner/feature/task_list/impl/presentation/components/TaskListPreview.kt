package com.github.igorergin.planner.feature.task_list.impl.presentation.components

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.github.igorergin.planner.feature.task_list.api.domain.model.TaskModel
import com.github.igorergin.planner.feature.task_list.impl.presentation.TaskListState
import com.github.planner.core.ui.theme.PlannerTheme
import java.time.LocalDate
import java.time.ZoneId

private val previewTasks = listOf(
    TaskModel(
        id = 1,
        title = "Утренняя зарядка",
        description = "15 минут упражнений",
        startTimestamp = LocalDate.now().atTime(8, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        finishTimestamp = LocalDate.now().atTime(8, 15).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        isCompleted = false
    ),
    TaskModel(
        id = 2,
        title = "Рабочая встреча",
        description = "Обсуждение проекта",
        startTimestamp = LocalDate.now().atTime(10, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        finishTimestamp = LocalDate.now().atTime(11, 30).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        isCompleted = false
    ),
    TaskModel(
        id = 3,
        title = "Обед",
        description = "",
        startTimestamp = LocalDate.now().atTime(13, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        finishTimestamp = LocalDate.now().atTime(14, 0).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
        isCompleted = true
    )
)

@Composable
private fun TaskListPreviewContent(
    state: TaskListState,
    darkTheme: Boolean = false
) {
    PlannerTheme(darkTheme = darkTheme) {
        Surface {
            TaskListContent(
                tasks = state.items,
                onTaskClick = {}
            )
        }
    }
}

@Preview(name = "List - Light", showBackground = true)
@Composable
fun PreviewTaskListLight() {
    TaskListPreviewContent(
        state = TaskListState(items = previewTasks)
    )
}

@Preview(name = "List - Dark", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun PreviewTaskListDark() {
    TaskListPreviewContent(
        state = TaskListState(items = previewTasks),
        darkTheme = true
    )
}

@Preview(name = "Empty State", showBackground = true)
@Composable
fun PreviewTaskListEmpty() {
    TaskListPreviewContent(
        state = TaskListState(items = emptyList())
    )
}

@Preview(name = "Header - Expanded", showBackground = true)
@Composable
fun PreviewHeaderExpanded() {
    PlannerTheme {
        CalendarPickerHeader(
            selectedDate = System.currentTimeMillis(),
            isExpanded = true,
            onDateSelected = {},
            onToggle = {}
        )
    }
}

@Preview(name = "Header - Collapsed", showBackground = true)
@Composable
fun PreviewHeaderCollapsed() {
    PlannerTheme {
        CalendarPickerHeader(
            selectedDate = System.currentTimeMillis(),
            isExpanded = false,
            onDateSelected = {},
            onToggle = {}
        )
    }
}
