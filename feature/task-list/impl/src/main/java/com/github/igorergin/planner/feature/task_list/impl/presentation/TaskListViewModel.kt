package com.github.igorergin.planner.feature.task_list.impl.presentation

import androidx.lifecycle.viewModelScope
import com.github.igorergin.planner.core.common.Dispatcher
import com.github.igorergin.planner.core.common.PlannerDispatchers
import com.github.igorergin.planner.core.common.result.Result
import com.github.igorergin.planner.core.common.result.asResult
import com.github.igorergin.planner.core.mvi.BaseViewModel
import com.github.igorergin.planner.feature.task_list.impl.domain.usecase.GetTasksForDayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksForDayUseCase: GetTasksForDayUseCase,
    @Dispatcher(PlannerDispatchers.IO) private val ioDispatcher: CoroutineDispatcher
) : BaseViewModel<TaskListState, TaskListIntent, TaskListEffect>(TaskListState()) {

    init {
        onIntent(TaskListIntent.LoadTasks)
    }

    override fun onIntent(intent: TaskListIntent) {
        when (intent) {
            is TaskListIntent.LoadTasks -> loadTasks()
            is TaskListIntent.OnDateSelected -> {
                updateState { copy(selectedDate = intent.timestamp) }
                loadTasks()
            }
            is TaskListIntent.OnAddTaskClick -> {
                sendEffect(TaskListEffect.NavigateToCreateTask)
            }
            is TaskListIntent.OnTaskClick -> {
                sendEffect(TaskListEffect.NavigateToDetails(intent.id))
            }
        }
    }

    private fun loadTasks() {
        getTasksForDayUseCase(currentState.selectedDate)
            .asResult()
            .onEach { result ->
                when (result) {
                    is Result.Loading -> updateState { copy(isLoading = true) }
                    is Result.Success -> {
                        updateState { copy(items = result.data, isLoading = false, error = null) }
                    }
                    is Result.Error -> {
                        updateState { copy(isLoading = false, error = result.exception?.message) }
                    }
                }
            }
            .launchIn(viewModelScope)
    }
}