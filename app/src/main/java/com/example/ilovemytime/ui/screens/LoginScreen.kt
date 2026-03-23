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
import com.example.ilovemytime.ui.components.PremiumButton
import com.example.ilovemytime.ui.components.PremiumTextField
import com.example.ilovemytime.viewmodel.TaskViewModel

@Composable
fun LoginScreen(
    viewModel: TaskViewModel,
    onNavigateToDashboard: () -> Unit
) {
    var name by remember { mutableStateOf("") }

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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Surface(
                modifier = Modifier.size(120.dp),
                shape = androidx.compose.foundation.shape.CircleShape,
                color = Color.White,
                shadowElevation = 8.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("LOGO", fontWeight = FontWeight.Black, color = Color(0xFF6A11CB))
                }
            }

            Text(
                "I Love My Time",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )

            PremiumTextField(
                value = name,
                onValueChange = { name = it },
                label = "Ingresa tu nombre",
                placeholder = "Ej. Robert",
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    if (name.isNotBlank()) {
                        viewModel.setUserName(name)
                        onNavigateToDashboard()
                    }
                })
            )

            PremiumButton(
                text = "Entrar",
                onClick = {
                    if (name.isNotBlank()) {
                        viewModel.setUserName(name)
                        onNavigateToDashboard()
                    }
                }
            )
        }
    }
}
