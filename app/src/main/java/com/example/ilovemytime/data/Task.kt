package com.example.ilovemytime.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import java.util.UUID

enum class TaskType {
    CICLO, ALARMA, TAREA_DEL_DIA, HABITO
}

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val name: String,
    val type: TaskType,
    val isNotificationEnabled: Boolean = false,
    val daysOfWeek: String = "",
    val startTime: String? = null,
    val duration: String? = null,
    val workTime: String? = null,
    val breakTime: String? = null,
    val cycles: Int? = null,
    val isCompleted: Boolean = false,
    val satisfactionResponse: String? = null
)