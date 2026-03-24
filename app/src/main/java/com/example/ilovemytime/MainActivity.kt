package com.example.ilovemytime

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ilovemytime.ui.theme.ILoveMyTimeTheme
import com.example.ilovemytime.viewmodel.TaskViewModel
import com.example.ilovemytime.viewmodel.TaskViewModelFactory // Asegúrate de tener esta importación
import com.example.ilovemytime.data.AppDatabase // Importación de tu base de datos
import com.example.ilovemytime.notifications.AlarmScheduler // Importación del scheduler
import com.example.ilovemytime.navigation.Screen
import com.example.ilovemytime.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ILoveMyTimeTheme {
                val context = LocalContext.current

                val database = AppDatabase.getDatabase(context)
                val factory = TaskViewModelFactory(database.taskDao())
                val viewModel: TaskViewModel = viewModel(factory = factory)

                val alarmScheduler = AlarmScheduler(context)

                val navController = rememberNavController()
                val activity = context as? MainActivity

                val permissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted -> }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        val permissionCheck = ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.POST_NOTIFICATIONS
                        )
                        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                val intentDestination = activity?.intent?.getStringExtra("DESTINATION")
                val intentTaskId = activity?.intent?.getStringExtra("TASK_ID")

                LaunchedEffect(intentDestination, intentTaskId) {
                    if (intentDestination == "satisfaction_form" && intentTaskId != null) {
                        navController.navigate(Screen.SatisfactionForm.createRoute(intentTaskId)) {
                            popUpTo(Screen.Dashboard.route) { inclusive = false }
                        }
                        activity.intent.removeExtra("DESTINATION")
                        activity.intent.removeExtra("TASK_ID")
                    }
                }

                NavHost(
                    navController = navController,
                    startDestination = Screen.Login.route
                ) {
                    composable(Screen.Login.route) {
                        LoginScreen(
                            viewModel = viewModel,
                            onNavigateToDashboard = {
                                navController.navigate(Screen.Dashboard.route) {
                                    popUpTo(Screen.Login.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Screen.Dashboard.route) {
                        DashboardScreen(
                            viewModel = viewModel,
                            onNavigateToAddTask = {
                                navController.navigate(Screen.TaskList.route) {
                                    popUpTo(Screen.Dashboard.route) { inclusive = true }
                                }
                                navController.navigate(Screen.AddTaskMenu.route)
                            },
                            onNavigateToTaskList = {
                                navController.navigate(Screen.TaskList.route) {
                                    popUpTo(Screen.Dashboard.route) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(Screen.TaskList.route) {
                        TaskListScreen(
                            viewModel = viewModel,
                            onNavigateToAddTask = {
                                navController.navigate(Screen.AddTaskMenu.route)
                            },
                            onNavigateToSatisfaction = { taskId ->
                                navController.navigate(Screen.SatisfactionForm.createRoute(taskId))
                            },
                            onNavigateToTimer = { taskId ->
                                navController.navigate(Screen.CycleTimer.createRoute(taskId))
                            },
                            onNavigateToNotifications = {
                                navController.navigate(Screen.Notifications.route)
                            },
                            onNavigateToProfile = {
                                navController.navigate(Screen.Profile.route)
                            }
                        )
                    }
                    composable(Screen.AddTaskMenu.route) {
                        AddTaskMenuScreen(
                            onNavigateToAddCycle = { navController.navigate(Screen.AddCycleTask.route) },
                            onNavigateToAddAlarm = { navController.navigate(Screen.AddAlarmTask.route) },
                            onNavigateToAddDaily = { navController.navigate(Screen.AddDailyTask.route) },
                            onNavigateToAddHabit = { navController.navigate(Screen.AddHabit.route) }
                        )
                    }
                    composable(Screen.AddCycleTask.route) {
                        AddCycleTaskScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.navigate(Screen.TaskList.route) { popUpTo(Screen.TaskList.route) { inclusive = true } } }
                        )
                    }
                    composable(Screen.AddAlarmTask.route) {
                        AddAlarmScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.navigate(Screen.TaskList.route) { popUpTo(Screen.TaskList.route) { inclusive = true } } }
                        )
                    }
                    composable(Screen.AddDailyTask.route) {
                        AddDailyTaskScreen(
                            viewModel = viewModel,
                            onNavigateBack = { navController.navigate(Screen.TaskList.route) { popUpTo(Screen.TaskList.route) { inclusive = true } } }
                        )
                    }

                    composable(Screen.AddHabit.route) {
                        AddHabitScreen(
                            viewModel = viewModel,
                            alarmScheduler = alarmScheduler,
                            onNavigateBack = { navController.navigate(Screen.TaskList.route) { popUpTo(Screen.TaskList.route) { inclusive = true } } }
                        )
                    }

                    composable(Screen.SatisfactionForm.route) { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                        SatisfactionFormScreen(
                            taskId = taskId,
                            viewModel = viewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }
                    composable(Screen.CycleTimer.route) { backStackEntry ->
                        val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                        CycleTimerScreen(
                            taskId = taskId,
                            viewModel = viewModel,
                            onNavigateToSatisfaction = { id ->
                                navController.navigate(Screen.SatisfactionForm.createRoute(id)) {
                                    popUpTo(Screen.TaskList.route)
                                }
                            }
                        )
                    }
                    composable(Screen.Notifications.route) {
                        NotificationsScreen(
                            onNavigateToTasks = { navController.navigate(Screen.TaskList.route) { popUpTo(0) } },
                            onNavigateToProfile = { navController.navigate(Screen.Profile.route) { popUpTo(Screen.TaskList.route) } }
                        )
                    }
                    composable(Screen.Profile.route) {
                        ProfileScreen(
                            viewModel = viewModel,
                            onNavigateToTasks = { navController.navigate(Screen.TaskList.route) { popUpTo(0) } },
                            onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) { popUpTo(Screen.TaskList.route) } }
                        )
                    }
                }
            }
        }
    }
}