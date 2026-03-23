package com.example.ilovemytime.data

import java.util.UUID

enum class TaskType {
    CICLO, ALARMA, TAREA_DEL_DIA, HABITO
}

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: TaskType,
    val isNotificationEnabled: Boolean = false,
    val daysOfWeek: List<String> = emptyList(),
    val startTime: String? = null,
    val duration: String? = null,
    val workTime: String? = null,
    val breakTime: String? = null,
    val cycles: Int? = null,
    val isCompleted: Boolean = false,
    val satisfactionResponse: String? = null
)
