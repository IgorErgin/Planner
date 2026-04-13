package com.github.igorergin.planner.feature.task_editor.impl.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import app.cash.turbine.test
import com.github.igorergin.planner.feature.task_editor.api.TaskEditorRoute
import com.github.igorergin.planner.feature.task_editor.impl.domain.model.EditorTaskModel
import com.github.igorergin.planner.feature.task_editor.impl.domain.usecase.GetTaskByIdUseCase
import com.github.igorergin.planner.feature.task_editor.impl.domain.usecase.SaveTaskUseCase
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TaskEditorViewModelTest {

    private val getTaskUseCase: GetTaskByIdUseCase = mockk()
    private val saveTaskUseCase: SaveTaskUseCase = mockk(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    // Используем мок вместо реального SavedStateHandle, чтобы не инициализировать Bundle
    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)

    private lateinit var viewModel: TaskEditorViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)

        // Мокаем статические расширения навигации, так как toRoute - это extension-функция
        mockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        // Обязательно снимаем мок со статических методов после каждого теста
        unmockkStatic("androidx.navigation.SavedStateHandleKt")
    }

    @Test
    fun `when taskId is provided, should load task data into state`() = runTest {
        // Given
        val mockRoute = TaskEditorRoute(taskId = 42)
        val mockTask = EditorTaskModel(
            id = 42,
            title = "Original Title",
            description = "Original Desc",
            dateStart = 1712822400000L,
            dateFinish = 1712826000000L
        )

        // Настраиваем поведение toRoute и UseCase
        every { savedStateHandle.toRoute<TaskEditorRoute>() } returns mockRoute
        coEvery { getTaskUseCase(42) } returns mockTask

        // When
        viewModel = TaskEditorViewModel(savedStateHandle, getTaskUseCase, saveTaskUseCase, testDispatcher)

        // Then
        viewModel.state.test {
            val state = awaitItem()
            assertEquals("Original Title", state.title)
            assertEquals(true, state.isEditMode)
            assertEquals(42, state.taskId)
        }
    }

    @Test
    fun `OnTitleChanged intent should update title in state`() = runTest {
        // Given
        every { savedStateHandle.toRoute<TaskEditorRoute>() } returns TaskEditorRoute(taskId = null)
        coEvery { getTaskUseCase(any()) } returns null

        viewModel = TaskEditorViewModel(savedStateHandle, getTaskUseCase, saveTaskUseCase, testDispatcher)

        // When
        viewModel.onIntent(TaskEditorIntent.OnTitleChanged("New Task"))

        // Then
        assertEquals("New Task", viewModel.state.value.title)
    }

    @Test
    fun `OnSaveClicked should trigger SaveTaskUseCase and send NavigateBack effect`() = runTest {
        // Given
        every { savedStateHandle.toRoute<TaskEditorRoute>() } returns TaskEditorRoute(taskId = null)
        coEvery { getTaskUseCase(any()) } returns null

        viewModel = TaskEditorViewModel(savedStateHandle, getTaskUseCase, saveTaskUseCase, testDispatcher)
        viewModel.onIntent(TaskEditorIntent.OnTitleChanged("Valid Title"))

        // When & Then
        viewModel.effect.test {
            viewModel.onIntent(TaskEditorIntent.OnSaveClicked)
            assertEquals(TaskEditorEffect.NavigateBack, awaitItem())
        }
    }
}