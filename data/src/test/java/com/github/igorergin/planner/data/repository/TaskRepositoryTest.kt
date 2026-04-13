package com.github.igorergin.planner.data.repository

import com.github.igorergin.planner.data.local.TaskDao
import com.github.igorergin.planner.data.model.TaskDto
import com.github.igorergin.planner.data.service.JsonParserService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class TaskRepositoryTest {

    private val taskDao: TaskDao = mockk(relaxed = true)
    private val jsonService: JsonParserService = mockk()
    private val repository = TaskRepository(taskDao, jsonService)

    @Test
    fun `prePopulateDataIfNeeded should insert tasks when database is empty`() = runTest {
        // Given: БД пуста
        every { taskDao.getTasksForDay(0, Long.MAX_VALUE) } returns flowOf(emptyList())

        // Имитируем данные из JSON
        val mockDtos = listOf(
            TaskDto(1, "1000", "2000", "Task from JSON", "Desc")
        )
        coEvery { jsonService.parseInitialJson() } returns mockDtos

        // When
        repository.prePopulateDataIfNeeded()

        // Then: Убеждаемся, что вызвана вставка в БД
        coVerify(exactly = 1) { taskDao.insertTasks(any()) }
    }

    @Test
    fun `prePopulateDataIfNeeded should NOT insert tasks when database is NOT empty`() = runTest {
        // Given: В БД уже есть 1 задача
        every { taskDao.getTasksForDay(0, Long.MAX_VALUE) } returns flowOf(listOf(mockk()))

        // When
        repository.prePopulateDataIfNeeded()

        // Then: Вставка не должна вызываться
        coVerify(exactly = 0) { jsonService.parseInitialJson() }
        coVerify(exactly = 0) { taskDao.insertTasks(any()) }
    }
}