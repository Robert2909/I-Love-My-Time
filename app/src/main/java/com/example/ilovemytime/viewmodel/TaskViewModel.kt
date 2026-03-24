package com.example.ilovemytime.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ilovemytime.data.Task
import com.example.ilovemytime.data.TaskDao
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TaskViewModel(private val taskDao: TaskDao) : ViewModel() {
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _searchQuery = MutableStateFlow("")

    val tasks: StateFlow<List<Task>> = combine(
        taskDao.getAllTasks(),
        _searchQuery
    ) { taskList, query ->
        if (query.isBlank()) taskList
        else taskList.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setUserName(name: String) { _userName.value = name }

    fun setSearchQuery(query: String) { _searchQuery.value = query }

    fun addTask(task: Task) {
        viewModelScope.launch {
            taskDao.insertTask(task)
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            tasks.value.find { it.id == taskId }?.let {
                taskDao.deleteTask(it)
            }
        }
    }

    fun deleteAllTasks() {
        viewModelScope.launch {
            taskDao.deleteAllTasks()
        }
    }

    fun updateSatisfaction(taskId: String, response: String) {
        viewModelScope.launch {
            tasks.value.find { it.id == taskId }?.let {
                val updatedTask = it.copy(isCompleted = true, satisfactionResponse = response)
                taskDao.insertTask(updatedTask)
            }
        }
    }
}

class TaskViewModelFactory(private val taskDao: TaskDao) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TaskViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TaskViewModel(taskDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}