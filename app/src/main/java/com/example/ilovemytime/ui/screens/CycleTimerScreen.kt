package com.example.ilovemytime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.example.ilovemytime.ui.components.PremiumButton
import com.example.ilovemytime.viewmodel.TaskViewModel
import kotlinx.coroutines.delay

@Composable
fun CycleTimerScreen(
    taskId: String,
    viewModel: TaskViewModel,
    onNavigateToSatisfaction: (String) -> Unit
) {
    val context = LocalContext.current
    val alarmScheduler = remember { com.example.ilovemytime.notifications.AlarmScheduler(context) }

    val tasks by viewModel.tasks.collectAsState()
    val task = tasks.find { it.id == taskId } ?: return

    var currentCycle by remember { mutableStateOf(1)}
    var isWorking by remember { mutableStateOf(true)}
    var timeLeft by remember { mutableStateOf((task.workTime?.toIntOrNull() ?: 25) * 60) }

    LaunchedEffect(isWorking, currentCycle) {
        val delaySeconds = if (isWorking) (task.workTime?.toIntOrNull() ?: 25) * 60 else (task.breakTime?.toIntOrNull() ?: 5) * 60

        val title = if (isWorking) "¡A descansar!" else "¡A trabajar!"
        val message = if (isWorking) "Terminaste un ciclo de ${task.name}" else "Se acabó el descanso, regresa a ${task.name}"

        alarmScheduler.scheduleTimerAlarm(taskId, title, message, delaySeconds)
    }

    DisposableEffect(Unit) {
        onDispose {
            alarmScheduler.cancelTimerAlarm(taskId)
        }
    }

    LaunchedEffect(key1 = timeLeft, key2 = isWorking) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft -= 1
        } else {
            if (isWorking) {
                isWorking = false
                timeLeft = (task.breakTime?.toIntOrNull() ?: 5) * 60
            } else {
                if (currentCycle < (task.cycles ?: 1)) {
                    currentCycle += 1
                    isWorking = true
                    timeLeft = (task.workTime?.toIntOrNull() ?: 25) * 60
                } else {
                    onNavigateToSatisfaction(taskId)
                }
            }
        }
    }

    val minutes = timeLeft / 60
    val seconds = timeLeft % 60

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = if (isWorking) listOf(Color(0xFF6A11CB), Color(0xFF2575FC)) else listOf(Color(0xFF4CAF50), Color(0xFF81C784))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally){
            Text(
                text = if (isWorking) "¡Trabajando!" else "¡Descanso!",
                fontSize = 40.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = String.format("%02d:%02d", minutes, seconds),
                fontSize = 80.sp,
                color = Color.White,
                fontWeight = FontWeight.Light
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Ciclo $currentCycle de ${task.cycles ?: 1}",
                fontSize = 24.sp,
                color = Color.White.copy(alpha = 0.8f)
            )
            Spacer(modifier = Modifier.height(64.dp))
            Button(
                onClick = { onNavigateToSatisfaction(taskId)},
                colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.3f))
            ) {
                Text("Omitir y terminar", color = Color.White)
            }
        }
    }
}