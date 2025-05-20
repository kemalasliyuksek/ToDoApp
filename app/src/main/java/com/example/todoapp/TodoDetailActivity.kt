package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.viewmodel.TodoDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoDetailActivity : AppCompatActivity() {
    private val viewModel: TodoDetailViewModel by viewModels()
    private var todoId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_detail)

        todoId = intent.getIntExtra("TODO_ID", -1)

        if (todoId == -1) {
            finish()
            return
        }

        setupObservers()
        setupEditButton()

        viewModel.loadTodo(todoId)
    }

    private fun setupObservers() {
        viewModel.todo.observe(this) { todo ->
            if (todo != null) {
                findViewById<TextView>(R.id.tvTodoIdValue).text = todo.id.toString()
                findViewById<TextView>(R.id.tvTodoNameValue).text = todo.name
            } else {
                finish()
            }
        }
    }

    private fun setupEditButton() {
        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            val intent = Intent(this, TodoEditActivity::class.java)
            intent.putExtra("TODO_ID", todoId)
            startActivityForResult(intent, MainActivity.REQUEST_EDIT_TODO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == MainActivity.REQUEST_EDIT_TODO && resultCode == RESULT_OK) {
            viewModel.loadTodo(todoId)
            setResult(RESULT_OK)
        }
    }
}