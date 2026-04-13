package com.github.igorergin.planner.data.service

import com.github.igorergin.planner.data.model.TaskDto


interface JsonParserService {
    suspend fun parseInitialJson(): List<TaskDto>
}