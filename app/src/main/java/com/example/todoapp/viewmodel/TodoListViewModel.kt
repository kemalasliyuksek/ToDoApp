package com.example.todoapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.data.TodoRepository
import com.example.todoapp.model.Todo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _todos = MutableLiveData<List<Todo>>()
    val todos: LiveData<List<Todo>> = _todos

    fun loadTodos() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getAllTodos()
            }
            _todos.value = result
        }
    }

    fun searchTodos(query: String) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                if (query.isEmpty()) {
                    repository.getAllTodos()
                } else {
                    repository.searchTodos(query)
                }
            }
            _todos.value = result
        }
    }

    fun deleteTodo(todoId: Int) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteTodo(todoId)
            }
            loadTodos()
        }
    }
}