package com.example.ilovemytime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
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
fun AddDailyTaskScreen(
    viewModel: TaskViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("0:00 am/pm") }
    var duration by remember { mutableStateOf("0:00") }
    var notify by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFE0C3FC), Color(0xFF8EC5FC))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Tarea del día",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            PremiumTextField(value = startTime, onValueChange = { startTime = it }, label = "Hora de comienza", placeholder = "Ej. 8:00 AM", keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
            PremiumTextField(value = duration, onValueChange = { duration = it }, label = "Duración", placeholder = "Ej. 1:30", keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
            
            val days = listOf("D", "L", "M", "Mi", "J", "V", "S")
            var selectedDays by remember { mutableStateOf(emptySet<String>()) }
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
                label = "Nombre tarea",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (name.isNotBlank()) {
                        viewModel.addTask(Task(name = name, type = TaskType.TAREA_DEL_DIA, startTime = startTime, duration = duration, isNotificationEnabled = notify))
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
                            viewModel.addTask(Task(name = name, type = TaskType.TAREA_DEL_DIA, startTime = startTime, duration = duration, isNotificationEnabled = notify))
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier.width(160.dp)
                )
            }
        }
    }
}
