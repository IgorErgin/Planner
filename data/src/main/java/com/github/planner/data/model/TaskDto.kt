package com.github.planner.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class TaskDto(
    @SerialName("id")
    val id: Int,

    @SerialName("date_start")
    val dateStart: String,

    @SerialName("date_finish")
    val dateFinish: String,

    @SerialName("name")
    val name: String,

    @SerialName("description")
    val description: String
)