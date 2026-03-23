package com.example.ilovemytime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ilovemytime.ui.components.PremiumButton
import com.example.ilovemytime.viewmodel.TaskViewModel

@Composable
fun SatisfactionFormScreen(
    taskId: String,
    viewModel: TaskViewModel,
    onNavigateBack: () -> Unit
) {
    var selectedEmoji by remember { mutableStateOf<String?>(null) }
    val emojis = listOf("😊", "😐", "😔")
    val labels = listOf("¡Sí, todo!", "Casi todo", "No pude hoy")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFfad0c4), Color(0xFFffd1ff))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                "¡Felicidades por completar tu actividad!",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF333333)
            )

            Text(
                "¿Lograste realizar todas tus actividades?",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = Color.DarkGray
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                emojis.forEachIndexed { index, emoji ->
                    val isSelected = selectedEmoji == emoji
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) Color.White.copy(alpha = 0.5f) else Color.Transparent)
                            .clickable { selectedEmoji = emoji }
                            .padding(8.dp)
                    ) {
                        Text(emoji, fontSize = 48.sp)
                        Text(labels[index], fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            PremiumButton(
                text = "Guardar Respuesta",
                onClick = {
                    if (selectedEmoji != null) {
                        viewModel.updateSatisfaction(taskId, selectedEmoji!!)
                        onNavigateBack()
                    }
                }
            )
        }
    }
}
