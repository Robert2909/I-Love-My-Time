package com.example.ilovemytime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
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
import com.example.ilovemytime.viewmodel.TaskViewModel
import com.example.ilovemytime.ui.components.PremiumTextField
import com.example.ilovemytime.ui.components.PremiumButton

@Composable
fun ProfileScreen(
    viewModel: TaskViewModel,
    onNavigateToTasks: () -> Unit,
    onNavigateToNotifications: () -> Unit
) {
    val currentName by viewModel.userName.collectAsState()
    var editingName by remember { mutableStateOf(currentName) }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                onNavigateToTasks = onNavigateToTasks,
                onNavigateToNotifications = onNavigateToNotifications,
                onNavigateToProfile = { },
                currentScreen = "profile"
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFFFD1FF), Color(0xFFF1E6FF))
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
                    "Mi Perfil",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = "Profile Photo",
                        modifier = Modifier.size(80.dp),
                        tint = Color.LightGray
                    )
                }

                PremiumTextField(
                    value = editingName,
                    onValueChange = { editingName = it },
                    label = "Cambiar nombre"
                )

                PremiumButton(
                    text = "Guardar Cambios",
                    onClick = {
                        if (editingName.isNotBlank()) {
                            viewModel.setUserName(editingName)
                        }
                    }
                )
                
                Text(
                    "Toca el icono para cambiar la foto (Próximamente)",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
