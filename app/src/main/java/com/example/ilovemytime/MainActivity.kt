package com.example.ilovemytime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.ilovemytime.ui.theme.ILoveMyTimeTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ilovemytime.viewmodel.TaskViewModel
import com.example.ilovemytime.navigation.Screen
import com.example.ilovemytime.ui.screens.LoginScreen
import com.example.ilovemytime.ui.screens.DashboardScreen
import com.example.ilovemytime.ui.screens.TaskListScreen
import com.example.ilovemytime.ui.screens.AddTaskMenuScreen
import com.example.ilovemytime.ui.screens.AddCycleTaskScreen
import com.example.ilovemytime.ui.screens.AddAlarmScreen
import com.example.ilovemytime.ui.screens.AddDailyTaskScreen
import com.example.ilovemytime.ui.screens.AddHabitScreen
import com.example.ilovemytime.ui.screens.SatisfactionFormScreen
import com.example.ilovemytime.ui.screens.CycleTimerScreen
import com.example.ilovemytime.ui.screens.NotificationsScreen
import com.example.ilovemytime.ui.screens.ProfileScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ILoveMyTimeTheme {
                val viewModel: TaskViewModel = viewModel()
                val navController = rememberNavController()

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

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ILoveMyTimeTheme {
        Greeting("Android")
    }
}