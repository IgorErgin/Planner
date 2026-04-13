package com.github.igorergin.planner.feature.task_editor.impl.domain.usecase

import com.github.igorergin.planner.data.local.TaskEntity
import com.github.igorergin.planner.data.repository.TaskRepository
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

class SaveTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(
        taskId: Int?,
        title: String,
        description: String,
        date: Long,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int
    ) {
        val zoneId = ZoneId.systemDefault()

        val localDate = Instant.ofEpochMilli(date)
            .atZone(zoneId)
            .toLocalDate()

        val startTime = LocalTime.of(startHour, startMinute)
        val endTime = LocalTime.of(endHour, endMinute)

        val startTs = LocalDateTime.of(localDate, startTime)
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()

        val endTs = LocalDateTime.of(localDate, endTime)
            .atZone(zoneId)
            .toInstant()
            .toEpochMilli()

        val taskEntity = TaskEntity(
            id = taskId ?: 0,
            name = title,
            description = description,
            dateStart = startTs,
            dateFinish = endTs
        )

        repository.saveTask(taskEntity)
    }
}