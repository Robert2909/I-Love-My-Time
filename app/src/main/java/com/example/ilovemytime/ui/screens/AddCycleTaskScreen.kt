package com.example.ilovemytime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun AddCycleTaskScreen(
    viewModel: TaskViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var workTime by remember { mutableStateOf("") }
    var breakTime by remember { mutableStateOf("") }
    var cycles by remember { mutableStateOf("") }
    var notify by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFACCBEE), Color(0xFFE7F0FD))
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
                "Tarea Ciclo",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            PremiumTextField(value = name, onValueChange = { name = it }, label = "Nombre actividad", placeholder = "Ej. Estudiar", keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
            PremiumTextField(value = workTime, onValueChange = { workTime = it }, label = "Tiempo de trabajo (min)", placeholder = "Ej. 25", keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = androidx.compose.ui.text.input.KeyboardType.Number))
            PremiumTextField(value = breakTime, onValueChange = { breakTime = it }, label = "Tiempo descanso (min)", placeholder = "Ej. 5", keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = androidx.compose.ui.text.input.KeyboardType.Number))
            PremiumTextField(
                value = cycles,
                onValueChange = { if (it.all { char -> char.isDigit() }) cycles = it },
                label = "Cantidad de ciclos",
                placeholder = "Ej. 4",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                keyboardActions = KeyboardActions(onDone = {
                    if (name.isNotBlank()) {
                        viewModel.addTask(com.example.ilovemytime.data.Task(name = name, type = com.example.ilovemytime.data.TaskType.CICLO, workTime = workTime, breakTime = breakTime, cycles = cycles.toIntOrNull(), isNotificationEnabled = notify))
                        onNavigateBack()
                    }
                })
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = notify, onCheckedChange = { notify = it })
                Text("Notificame", fontWeight = FontWeight.SemiBold)
            }

            PremiumButton(
                text = "Agregar",
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.addTask(Task(name = name, type = TaskType.CICLO, workTime = workTime, breakTime = breakTime, cycles = cycles.toIntOrNull(), isNotificationEnabled = notify))
                        onNavigateBack()
                    }
                }
            )
        }
    }
}
