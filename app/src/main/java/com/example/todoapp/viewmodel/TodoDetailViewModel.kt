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
class TodoDetailViewModel @Inject constructor(
    private val repository: TodoRepository
) : ViewModel() {

    private val _todo = MutableLiveData<Todo?>()
    val todo: LiveData<Todo?> = _todo

    fun loadTodo(todoId: Int) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getTodo(todoId)
            }
            _todo.value = result
        }
    }
}