package com.example.ilovemytime.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Dashboard : Screen("dashboard")
    object TaskList : Screen("task_list")
    object AddTaskMenu : Screen("add_task_menu")
    object AddCycleTask : Screen("add_cycle_task")
    object AddAlarmTask : Screen("add_alarm_task")
    object AddDailyTask : Screen("add_daily_task")
    object AddHabit : Screen("add_habit")
    object SatisfactionForm : Screen("satisfaction_form/{taskId}") {
        fun createRoute(taskId: String) = "satisfaction_form/$taskId"
    }
    object CycleTimer : Screen("cycle_timer/{taskId}") {
        fun createRoute(taskId: String) = "cycle_timer/$taskId"
    }
    object Notifications : Screen("notifications")
    object Profile : Screen("profile")
}
