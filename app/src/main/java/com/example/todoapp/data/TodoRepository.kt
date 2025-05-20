package com.example.todoapp.data

import com.example.todoapp.model.Todo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRepository @Inject constructor(
    private val todoDbHelper: TodoDbHelper
) {
    fun getAllTodos(): List<Todo> {
        return todoDbHelper.getAllTodos()
    }

    fun addTodo(todo: Todo): Long {
        return todoDbHelper.addTodo(todo)
    }

    fun updateTodo(todo: Todo): Int {
        return todoDbHelper.updateTodo(todo)
    }

    fun deleteTodo(todoId: Int): Int {
        return todoDbHelper.deleteTodo(todoId)
    }

    fun getTodo(todoId: Int): Todo? {
        return todoDbHelper.getTodo(todoId)
    }

    fun searchTodos(query: String): List<Todo> {
        return todoDbHelper.searchTodos(query)
    }
}