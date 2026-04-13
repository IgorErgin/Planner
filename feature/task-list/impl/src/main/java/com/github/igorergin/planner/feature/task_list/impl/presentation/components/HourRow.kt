package com.github.igorergin.planner.feature.task_list.impl.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.igorergin.planner.feature.task_list.api.domain.model.TaskModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

/**
 * Строка таблицы для одного часа (например, 10:00 - 11:00)
 */
@Composable
fun HourRow(
    hour: Int,
    tasks: List<TaskModel>,
    onTaskClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 72.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "%02d.00-%02d.00".format(hour, (hour + 1) % 24),
            modifier = Modifier
                .width(100.dp)
                .padding(top = 8.dp, start = 8.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 4.dp)
        ) {
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth(),
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                val timeFormatter = remember { DateTimeFormatter.ofPattern("HH:mm") }
                val zoneId = remember { ZoneId.systemDefault() }

                tasks.forEach { task ->
                    TaskCard(
                        task = task,
                        timeFormatter = timeFormatter,
                        zoneId = zoneId,
                        onClick = { onTaskClick(task.id) }
                    )
                }
            }
        }
    }
}
