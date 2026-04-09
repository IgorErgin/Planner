package com.github.planner.data.service

import com.github.planner.data.model.TaskDto


interface JsonParserService {
    suspend fun parseInitialJson(): List<TaskDto>
}