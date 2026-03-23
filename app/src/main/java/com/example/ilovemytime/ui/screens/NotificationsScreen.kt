package com.example.ilovemytime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ilovemytime.ui.screens.BottomNavigationBar

@Composable
fun NotificationsScreen(
    onNavigateToTasks: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onNavigateToTasks = onNavigateToTasks,
                onNavigateToNotifications = { },
                onNavigateToProfile = onNavigateToProfile,
                currentScreen = "notifications"
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFE0C3FC), Color(0xFF8EC5FC))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Notificaciones",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "No hay notificaciones por el momento",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
