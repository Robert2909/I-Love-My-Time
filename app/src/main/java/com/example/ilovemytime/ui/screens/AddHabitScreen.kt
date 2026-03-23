package com.example.ilovemytime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.ilovemytime.data.Task
import com.example.ilovemytime.data.TaskType
import com.example.ilovemytime.ui.components.PremiumButton
import com.example.ilovemytime.ui.components.PremiumTextField
import com.example.ilovemytime.viewmodel.TaskViewModel

@Composable
fun AddHabitScreen(
    viewModel: TaskViewModel,
    onNavigateBack: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var frequency by remember { mutableStateOf("Diario") }
    var notify by remember { mutableStateOf(true) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF84fab0), Color(0xFF8fd3f4))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                "Nuevo Hábito",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            PremiumTextField(value = name, onValueChange = { name = it }, label = "Nombre del hábito", placeholder = "Ej. Beber agua", keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next))
            PremiumTextField(
                value = frequency,
                onValueChange = { frequency = it },
                label = "Frecuencia",
                placeholder = "Ej. Diario, Lunes...",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (name.isNotBlank()) {
                        viewModel.addTask(Task(name = name, type = TaskType.HABITO, isNotificationEnabled = notify))
                        onNavigateBack()
                    }
                })
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = notify, onCheckedChange = { notify = it })
                Text("Notificame", fontWeight = FontWeight.SemiBold)
            }

            PremiumButton(
                text = "Guardar",
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.addTask(Task(name = name, type = TaskType.HABITO, isNotificationEnabled = notify))
                        onNavigateBack()
                    }
                }
            )
        }
    }
}
