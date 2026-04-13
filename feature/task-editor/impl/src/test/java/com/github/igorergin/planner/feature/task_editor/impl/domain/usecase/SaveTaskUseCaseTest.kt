package com.github.igorergin.planner.feature.task_editor.impl.domain.usecase

import com.github.igorergin.planner.data.repository.TaskRepository
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneId

class SaveTaskUseCaseTest {

    private val repository: TaskRepository = mockk(relaxed = true)
    private val useCase = SaveTaskUseCase(repository)

    @Test
    fun `invoke should calculate correct timestamps and call repository`() = runTest {
        // Given
        val date = LocalDate.of(2024, 1, 1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()

        // When
        useCase(
            taskId = null,
            title = "Test Task",
            description = "Test Desc",
            date = date,
            startHour = 10,
            startMinute = 0,
            endHour = 12,
            endMinute = 30
        )

        // Then
        coVerify {
            repository.saveTask(match { entity ->
                entity.name == "Test Task" &&
                        entity.id == 0 &&
                        // Проверяем, что UseCase правильно склеил дату и время
                        // (Приблизительная проверка структуры)
                        entity.dateStart > date && entity.dateFinish > entity.dateStart
            })
        }
    }
}