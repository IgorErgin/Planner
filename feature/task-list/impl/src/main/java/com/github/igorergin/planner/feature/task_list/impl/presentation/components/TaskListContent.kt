package com.github.igorergin.planner.feature.task_list.impl.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.igorergin.planner.feature.task_list.api.domain.model.TaskModel
import com.github.igorergin.planner.feature.task_list.impl.R
import java.time.Instant
import java.time.ZoneId

@Composable
fun TaskListContent(
    tasks: List<TaskModel>,
    onTaskClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    val tasksByHour = remember(tasks) {
        val zoneId = ZoneId.systemDefault()
        tasks.groupBy { task ->
            Instant.ofEpochMilli(task.startTimestamp).atZone(zoneId).hour
        }
    }

    if (tasks.isEmpty()) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = stringResource(R.string.task_list_empty_state),
                color = MaterialTheme.colorScheme.outline
            )
        }
    } else {
        LazyColumn(
            state = scrollState,
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            items((0..23).toList()) { hour ->
                val tasksInHour = tasksByHour[hour] ?: emptyList()
                HourRow(hour = hour, tasks = tasksInHour, onTaskClick = onTaskClick)
            }
        }
    }
}
