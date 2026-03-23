package com.example.ilovemytime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ilovemytime.data.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn

class TaskViewModel : ViewModel() {
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    val tasks: kotlinx.coroutines.flow.StateFlow<List<Task>> = kotlinx.coroutines.flow.combine(_tasks, _searchQuery) { taskList, query ->
        if (query.isBlank()) taskList else taskList.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000), emptyList())

    fun setUserName(name: String) {
        _userName.value = name
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addTask(task: Task) {
        _tasks.value += task
    }

    fun deleteTask(taskId: String) {
        _tasks.value = _tasks.value.filterNot { it.id == taskId }
    }

    fun deleteAllTasks() {
        _tasks.value = emptyList()
    }

    fun updateSatisfaction(taskId: String, response: String) {
        _tasks.value = _tasks.value.map {
            if (it.id == taskId) {
                it.copy(isCompleted = true, satisfactionResponse = response)
            } else it
        }
    }
}
