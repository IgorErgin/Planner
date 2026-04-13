package com.github.igorergin.planner.feature.task_list.impl.domain.usecase

import com.github.igorergin.planner.data.local.TaskEntity
import com.github.igorergin.planner.data.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetTasksForDayUseCaseTest {

    private val repository: TaskRepository = mockk()
    private val useCase = GetTasksForDayUseCase(repository)

    @Test
    fun `invoke should return mapped domain models from repository entities`() = runTest {
        // Given
        val selectedDate = 1000L
        val dayEnd = selectedDate + 86400000L
        val entities = listOf(
            TaskEntity(id = 1, name = "Task 1", description = "Desc 1", dateStart = 1001L, dateFinish = 1050L),
            TaskEntity(id = 2, name = "Task 2", description = "Desc 2", dateStart = 2000L, dateFinish = 2100L)
        )

        coEvery { repository.getTasksForDay(selectedDate, dayEnd) } returns flowOf(entities)

        // When
        val result = useCase(selectedDate).first()

        // Then
        assertEquals(2, result.size)
        assertEquals("Task 1", result[0].title)
        assertEquals(1, result[0].id)
        assertEquals(false, result[0].isCompleted)
    }

    @Test
    fun `invoke with empty repository should return empty list`() = runTest {
        // Given
        coEvery { repository.getTasksForDay(any(), any()) } returns flowOf(emptyList())

        // When
        val result = useCase(0L).first()

        // Then
        assertEquals(0, result.size)
    }
}