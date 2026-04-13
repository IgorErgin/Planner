package com.github.igorergin.planner.feature.task_editor.impl.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.igorergin.planner.feature.task_editor.impl.R
import com.github.igorergin.planner.feature.task_editor.impl.presentation.TaskEditorIntent
import com.github.igorergin.planner.feature.task_editor.impl.presentation.TaskEditorState
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditorContent(
    state: TaskEditorState,
    onIntent: (TaskEditorIntent) -> Unit,
    onBack: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd.MM.yyyy") }
    val zoneId = remember { ZoneId.systemDefault() }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = if (state.date == 0L) System.currentTimeMillis() else state.date
    )
    val startTimeState = rememberTimePickerState(initialHour = state.startHour, initialMinute = state.startMinute)
    val endTimeState = rememberTimePickerState(initialHour = state.endHour, initialMinute = state.endMinute)

    if (showDatePicker) {
        TaskDatePicker(
            state = datePickerState,
            onConfirm = { onIntent(TaskEditorIntent.OnDateSelected(it)) },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showStartTimePicker) {
        TaskTimePicker(
            state = startTimeState,
            onConfirm = { hour, minute ->
                onIntent(TaskEditorIntent.OnStartTimeSelected(hour, minute))
            },
            onDismiss = { showStartTimePicker = false }
        )
    }

    if (showEndTimePicker) {
        TaskTimePicker(
            state = endTimeState,
            onConfirm = { hour, minute ->
                onIntent(TaskEditorIntent.OnEndTimeSelected(hour, minute))
            },
            onDismiss = { showEndTimePicker = false }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (state.isEditMode) stringResource(R.string.task_editor_title_edit)
                        else stringResource(R.string.task_editor_title_new)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = state.title,
                onValueChange = { onIntent(TaskEditorIntent.OnTitleChanged(it)) },
                label = { Text(stringResource(R.string.task_editor_label_title)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedButton(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.DateRange, contentDescription = null)
                Spacer(Modifier.width(8.dp))

                val dateStr = remember(state.date) {
                    if (state.date == 0L) null
                    else Instant.ofEpochMilli(state.date).atZone(zoneId).format(dateFormatter)
                }
                Text(dateStr ?: stringResource(R.string.task_editor_select_date))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val startTimeStr = remember(state.startHour, state.startMinute) {
                    LocalTime.of(state.startHour, state.startMinute).toString()
                }
                val endTimeStr = remember(state.endHour, state.endMinute) {
                    LocalTime.of(state.endHour, state.endMinute).toString()
                }

                OutlinedButton(onClick = { showStartTimePicker = true }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.task_editor_start_time_prefix, startTimeStr))
                }
                OutlinedButton(onClick = { showEndTimePicker = true }, modifier = Modifier.weight(1f)) {
                    Text(stringResource(R.string.task_editor_end_time_prefix, endTimeStr))
                }
            }

            OutlinedTextField(
                value = state.description,
                onValueChange = { onIntent(TaskEditorIntent.OnDescriptionChanged(it)) },
                label = { Text(stringResource(R.string.task_editor_label_description)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onIntent(TaskEditorIntent.OnSaveClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state.title.isNotBlank() && !state.isSaving
            ) {
                if (state.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        if (state.isEditMode) stringResource(R.string.task_editor_button_save)
                        else stringResource(R.string.task_editor_button_create)
                    )
                }
            }
        }
    }
}
