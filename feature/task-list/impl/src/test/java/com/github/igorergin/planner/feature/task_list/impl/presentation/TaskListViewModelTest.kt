package com.github.igorergin.planner.feature.task_list.impl.presentation

import app.cash.turbine.test
import com.github.igorergin.planner.feature.task_list.api.domain.model.TaskModel
import com.github.igorergin.planner.feature.task_list.impl.domain.usecase.GetTasksForDayUseCase
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskListViewModelTest {

    private val getTasksUseCase: GetTasksForDayUseCase = mockk()
    private val testDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: TaskListViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial load should update state with tasks from use case`() = runTest {
        // Given
        val mockTasks = listOf(
            TaskModel(1, "Title", "Desc", 0L, 100L, false)
        )
        every { getTasksUseCase(any()) } returns flowOf(mockTasks)

        // When
        viewModel = TaskListViewModel(getTasksUseCase, testDispatcher)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals(mockTasks, state.items)
            assertEquals(false, state.isLoading)
        }
    }

    @Test
    fun `OnAddTaskClick intent should send NavigateToCreateTask effect`() = runTest {
        // Given
        every { getTasksUseCase(any()) } returns flowOf(emptyList())
        viewModel = TaskListViewModel(getTasksUseCase, testDispatcher)

        // When & Then
        viewModel.effect.test {
            viewModel.onIntent(TaskListIntent.OnAddTaskClick)
            assertEquals(TaskListEffect.NavigateToCreateTask, awaitItem())
        }
    }

    @Test
    fun `OnTaskClick intent should send NavigateToDetails effect with correct ID`() = runTest {
        // Given
        val taskId = 42
        every { getTasksUseCase(any()) } returns flowOf(emptyList())
        viewModel = TaskListViewModel(getTasksUseCase, testDispatcher)

        // When & Then
        viewModel.effect.test {
            viewModel.onIntent(TaskListIntent.OnTaskClick(taskId))
            val effect = awaitItem() as TaskListEffect.NavigateToDetails
            assertEquals(taskId, effect.taskId)
        }
    }
}