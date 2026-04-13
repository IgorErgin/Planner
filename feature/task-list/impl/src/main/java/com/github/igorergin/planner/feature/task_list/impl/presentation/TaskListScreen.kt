package com.github.igorergin.planner.feature.task_list.impl.presentation

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.igorergin.planner.feature.task_list.impl.presentation.components.CalendarPickerHeader
import com.github.igorergin.planner.feature.task_list.impl.presentation.components.TaskListContent

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel,
    onNavigateToEditor: (Int?) -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is TaskListEffect.NavigateToDetails -> onNavigateToEditor(effect.taskId)
                is TaskListEffect.NavigateToCreateTask -> onNavigateToEditor(null)
            }
        }
    }

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    var isCalendarExpanded by remember { mutableStateOf(true) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onIntent(TaskListIntent.OnAddTaskClick) },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val onDateSelected: (Long) -> Unit = { timestamp ->
                viewModel.onIntent(TaskListIntent.OnDateSelected(timestamp))
                isCalendarExpanded = false
            }

            if (isLandscape) {
                LandscapeLayout(
                    state = state,
                    isCalendarExpanded = isCalendarExpanded,
                    onDateSelected = onDateSelected,
                    onTaskClick = { viewModel.onIntent(TaskListIntent.OnTaskClick(it)) },
                    onToggleCalendar = { isCalendarExpanded = !isCalendarExpanded }
                )
            } else {
                PortraitLayout(
                    state = state,
                    isCalendarExpanded = isCalendarExpanded,
                    onDateSelected = onDateSelected,
                    onTaskClick = { viewModel.onIntent(TaskListIntent.OnTaskClick(it)) },
                    onToggleCalendar = { isCalendarExpanded = !isCalendarExpanded }
                )
            }
        }
    }
}

@Composable
private fun PortraitLayout(
    state: TaskListState,
    isCalendarExpanded: Boolean,
    onDateSelected: (Long) -> Unit,
    onTaskClick: (Int) -> Unit,
    onToggleCalendar: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        CalendarPickerHeader(
            selectedDate = state.selectedDate,
            isExpanded = isCalendarExpanded,
            onDateSelected = onDateSelected,
            onToggle = onToggleCalendar
        )

        AnimatedVisibility(
            visible = !isCalendarExpanded,
            modifier = Modifier.weight(1f),
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = fadeOut()
        ) {
            TaskListContent(
                tasks = state.items,
                onTaskClick = onTaskClick
            )
        }
    }
}

@Composable
private fun LandscapeLayout(
    state: TaskListState,
    isCalendarExpanded: Boolean,
    onDateSelected: (Long) -> Unit,
    onTaskClick: (Int) -> Unit,
    onToggleCalendar: () -> Unit
) {
    Row(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .weight(if (isCalendarExpanded) 1.2f else 0.6f)
                .fillMaxHeight()
                .animateContentSize(animationSpec = tween(400))
        ) {
            CalendarPickerHeader(
                selectedDate = state.selectedDate,
                isExpanded = isCalendarExpanded,
                onDateSelected = onDateSelected,
                onToggle = onToggleCalendar
            )
        }

        VerticalDivider(
            modifier = Modifier.fillMaxHeight(),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outlineVariant
        )

        Box(modifier = Modifier.weight(2f)) {
            TaskListContent(
                tasks = state.items,
                onTaskClick = onTaskClick
            )
        }
    }
}


