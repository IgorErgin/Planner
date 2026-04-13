package com.github.igorergin.planner.feature.task_editor.impl.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.github.igorergin.planner.core.common.Dispatcher
import com.github.igorergin.planner.core.common.PlannerDispatchers
import com.github.igorergin.planner.core.mvi.BaseViewModel
import com.github.igorergin.planner.feature.task_editor.api.TaskEditorRoute
import com.github.igorergin.planner.feature.task_editor.impl.domain.usecase.GetTaskByIdUseCase
import com.github.igorergin.planner.feature.task_editor.impl.domain.usecase.SaveTaskUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

@HiltViewModel
class TaskEditorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val saveTaskUseCase: SaveTaskUseCase,
    @Dispatcher(PlannerDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<TaskEditorState, TaskEditorIntent, TaskEditorEffect>(TaskEditorState()) {

    private val route: TaskEditorRoute = savedStateHandle.toRoute<TaskEditorRoute>()

    init {
        route.taskId?.let { loadTaskData(it) }
    }

    private fun loadTaskData(id: Int) {
        viewModelScope.launch(ioDispatcher) {
            val task = getTaskByIdUseCase(id)
            if (task != null) {
                val dateTime = Instant.ofEpochMilli(task.dateStart)
                    .atZone(ZoneId.systemDefault())

                val endDateTime = Instant.ofEpochMilli(task.dateFinish)
                    .atZone(ZoneId.systemDefault())

                updateState {
                    copy(
                        taskId = task.id,
                        title = task.title,
                        description = task.description,
                        date = task.dateStart,
                        startHour = dateTime.hour,
                        startMinute = dateTime.minute,
                        endHour = endDateTime.hour,
                        endMinute = endDateTime.minute,
                        isEditMode = true
                    )
                }
            }
        }
    }

    override fun onIntent(intent: TaskEditorIntent) {
        when (intent) {
            is TaskEditorIntent.OnTitleChanged -> updateState { copy(title = intent.value) }
            is TaskEditorIntent.OnDescriptionChanged -> updateState { copy(description = intent.value) }
            is TaskEditorIntent.OnDateSelected -> updateState { copy(date = intent.timestamp) }
            is TaskEditorIntent.OnStartTimeSelected -> updateState {
                copy(startHour = intent.hour, startMinute = intent.minute)
            }
            is TaskEditorIntent.OnEndTimeSelected -> updateState {
                copy(endHour = intent.hour, endMinute = intent.minute)
            }
            is TaskEditorIntent.OnSaveClicked -> saveTask()
        }
    }

    private fun saveTask() {
        val s = currentState
        if (s.title.isBlank()) return

        updateState { copy(isSaving = true) }

        viewModelScope.launch(ioDispatcher) {
            try {
                saveTaskUseCase(
                    taskId = s.taskId,
                    title = s.title,
                    description = s.description,
                    date = s.date,
                    startHour = s.startHour,
                    startMinute = s.startMinute,
                    endHour = s.endHour,
                    endMinute = s.endMinute
                )
                sendEffect(TaskEditorEffect.NavigateBack)
            } catch (e: Exception) {
                updateState { copy(isSaving = false) }
            }
        }
    }
}