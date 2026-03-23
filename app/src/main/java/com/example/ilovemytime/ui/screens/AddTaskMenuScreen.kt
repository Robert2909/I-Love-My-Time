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
import com.example.ilovemytime.ui.components.PremiumButton

@Composable
fun AddTaskMenuScreen(
    onNavigateToAddCycle: () -> Unit,
    onNavigateToAddAlarm: () -> Unit,
    onNavigateToAddDaily: () -> Unit,
    onNavigateToAddHabit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFF9A9E), Color(0xFFFEE140))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                "Nueva Tarea",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Text(
                "Selecciona el tipo de actividad",
                fontSize = 18.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(24.dp))

            PremiumButton(text = "Ciclo", onClick = onNavigateToAddCycle)
            PremiumButton(text = "Alarma", onClick = onNavigateToAddAlarm)
            PremiumButton(text = "Tarea del dia", onClick = onNavigateToAddDaily)
            PremiumButton(text = "Habito", onClick = onNavigateToAddHabit)
        }
    }
}
