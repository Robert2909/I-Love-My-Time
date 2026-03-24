package com.example.ilovemytime.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ilovemytime.viewmodel.TaskViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel,
    onNavigateToAddTask: () -> Unit,
    onNavigateToSatisfaction: (String) -> Unit,
    onNavigateToTimer: (String) -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val tasks by viewModel.tasks.collectAsState()
    var isSearchVisible by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val haptic = LocalHapticFeedback.current

    val filteredTasks = tasks.filter {
        it.name.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (isSearchVisible) {
                androidx.compose.material3.TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search tasks") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            searchQuery = ""
                            isSearchVisible = false
                        }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
        },
        bottomBar = {
            BottomNavigationBar(
                onNavigateToTasks = { },
                onNavigateToNotifications = onNavigateToNotifications,
                onNavigateToProfile = onNavigateToProfile,
                currentScreen = "tasks"
            )
        },
        floatingActionButton = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.End
            ) {
                FloatingActionButton(onClick = { isSearchVisible = !isSearchVisible }, containerColor = Color(0xFF6A11CB), contentColor = Color.White) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Long press to delete all tasks",
                                duration = SnackbarDuration.Short
                            )
                        }
                    },
                    modifier = Modifier.combinedClickable(
                        onLongClick = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            viewModel.deleteAllTasks()
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "All tasks deleted",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        },
                        onClick = {
                            scope.launch {
                                snackbarHostState.showSnackbar(
                                    message = "Long press to delete all tasks",
                                    duration = SnackbarDuration.Short
                                )
                            }
                        }
                    ),
                    containerColor = Color(0xFFE91E63),
                    contentColor = Color.White
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete All")
                }
                FloatingActionButton(onClick = onNavigateToAddTask, containerColor = Color(0xFF4CAF50), contentColor = Color.White) {
                    Icon(Icons.Default.Add, contentDescription = "Add")
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFFAFAFA), Color(0xFFE0E0E0))
                    )
                )
        ) {
            if (filteredTasks.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.List,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = Color.LightGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay tareas pendientes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Text(
                        text = "¡Presiona el botón + para comenzar!",
                        fontSize = 14.sp,
                        color = Color.Gray.copy(alpha = 0.6f)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filteredTasks) { task ->
                        TaskItem(
                            task = task,
                            onAction = {
                                if (task.type == com.example.ilovemytime.data.TaskType.CICLO) {
                                    onNavigateToTimer(task.id)
                                } else {
                                    onNavigateToSatisfaction(task.id)
                                }
                            },
                            onDelete = { taskId -> viewModel.deleteTask(taskId) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskItem(
    task: com.example.ilovemytime.data.Task,
    onAction: () -> Unit,
    onDelete: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    if (showMenu) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showMenu = false },
            title = { Text("Eliminar Tarea") },
            text = { Text("¿Estás seguro de que deseas eliminar esta tarea?") },
            confirmButton = {
                TextButton(onClick = { onDelete(task.id); showMenu = false }) {
                    Text("Eliminar", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showMenu = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {},
                onLongClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    showMenu = true
                }
            ),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        color = if (task.isCompleted) Color(0xFFF1F8E9) else Color.White,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    task.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                Text(
                    task.type.name,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                if (task.satisfactionResponse != null) {
                    Text("Logrado: ${task.satisfactionResponse}", color = Color(0xFF4CAF50), fontSize = 12.sp)
                }
            }

            Button(
                onClick = onAction,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (task.type == com.example.ilovemytime.data.TaskType.CICLO) Color(0xFF6A11CB) else Color(0xFF2575FC)
                )
            ) {
                Text(if (task.type == com.example.ilovemytime.data.TaskType.CICLO) "Iniciar" else "Ver Formulario")
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    onNavigateToTasks: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    onNavigateToProfile: () -> Unit,
    currentScreen: String
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFF6A11CB)
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.DateRange, contentDescription = "Tasks") },
            selected = currentScreen == "tasks",
            onClick = onNavigateToTasks
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = "Notifications") },
            selected = currentScreen == "notifications",
            onClick = onNavigateToNotifications
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "Profile") },
            selected = currentScreen == "profile",
            onClick = onNavigateToProfile
        )
    }
}
