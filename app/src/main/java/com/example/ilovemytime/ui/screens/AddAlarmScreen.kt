package com.example.ilovemytime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction

import com.example.ilovemytime.data.Task
import com.example.ilovemytime.data.TaskType
import com.example.ilovemytime.ui.components.PremiumButton
import com.example.ilovemytime.ui.components.PremiumTextField
import com.example.ilovemytime.viewmodel.TaskViewModel

@Composable
fun AddAlarmScreen(
    viewModel: TaskViewModel,
    onNavigateBack: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val alarmScheduler = remember { com.example.ilovemytime.notifications.AlarmScheduler(context) }
    var name by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("0:00 am") }
    var selectedDays by remember { mutableStateOf(emptySet<String>()) }
    var notify by remember { mutableStateOf(false) }

    val days = listOf("D", "L", "M", "Mi", "J", "V", "S")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFD1D1), Color(0xFFFBE2E2))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "Nueva Alarma",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            PremiumTextField(
                value = time,
                onValueChange = { time = it },
                label = "Hora (Ej. 8:00 AM)",
                placeholder = "8:00 AM",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEach { day ->
                    val isSelected = selectedDays.contains(day)
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFF6A11CB) else Color.White)
                            .clickable {
                                selectedDays = if (isSelected) selectedDays - day else selectedDays + day
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            day,
                            color = if (isSelected) Color.White else Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            PremiumTextField(
                value = name,
                onValueChange = { name = it },
                label = "Nombre alarma",
                placeholder = "Ej. Despertar",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (name.isNotBlank()) {
                        viewModel.addTask(Task(name = name, type = TaskType.ALARMA, startTime = time, isNotificationEnabled = notify))
                        onNavigateBack()
                    }
                })
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = notify, onCheckedChange = { notify = it })
                Text("Notificame", fontWeight = FontWeight.SemiBold)
            }

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                TextButton(onClick = onNavigateBack) {
                    Text("Cancelar", fontSize = 18.sp, color = Color.Gray)
                }
                PremiumButton(
                    text = "Guardar",
                    onClick = {
                        if (name.isNotBlank()) {
                            val newTask = Task(name = name, type = TaskType.ALARMA, startTime = time, isNotificationEnabled = notify)
                            viewModel.addTask(newTask)

                            if (notify) {
                                alarmScheduler.scheduleAlarm(taskId = newTask.id, taskName = newTask.name, timeString = newTask.startTime)
                            }

                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.width(160.dp)
                )
            }
        }
    }
}
