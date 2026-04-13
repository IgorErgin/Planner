package com.github.igorergin.planner.feature.task_editor.impl.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.github.igorergin.planner.feature.task_editor.impl.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskTimePicker(
    state: TimePickerState,
    onConfirm: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onConfirm(state.hour, state.minute)
                onDismiss()
            }) {
                Text(stringResource(R.string.common_ok))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(android.R.string.cancel))
            }
        },
        text = {
            TimePicker(state = state)
        }
    )
}
