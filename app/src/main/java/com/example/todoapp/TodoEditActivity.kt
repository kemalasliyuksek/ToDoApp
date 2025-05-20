package com.example.todoapp

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.TodoEditViewModel
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoEditActivity : AppCompatActivity() {
    private val viewModel: TodoEditViewModel by viewModels()
    private lateinit var etTodoName: TextInputEditText
    private var todoId: Int = -1
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo_edit)

        etTodoName = findViewById(R.id.etTodoName)

        todoId = intent.getIntExtra("TODO_ID", -1)
        isEditMode = todoId != -1

        setupUI()
        setupObservers()
        setupSaveButton()

        if (isEditMode) {
            viewModel.loadTodo(todoId)
        }
    }

    private fun setupUI() {
        val titleTextView = findViewById<TextView>(R.id.tvTitle)

        if (isEditMode) {
            titleTextView.text = getString(R.string.edit_todo)
        } else {
            titleTextView.text = getString(R.string.add_todo)
        }
    }

    private fun setupObservers() {
        viewModel.todo.observe(this) { todo ->
            if (todo != null) {
                etTodoName.setText(todo.name)
            }
        }

        viewModel.operationComplete.observe(this) { complete ->
            if (complete) {
                val messageResId = if (isEditMode) R.string.todo_updated else R.string.todo_added
                Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun setupSaveButton() {
        findViewById<Button>(R.id.btnSave).setOnClickListener {
            val todoName = etTodoName.text.toString().trim()

            if (todoName.isEmpty()) {
                etTodoName.error = "Yapılacak ismi boş olamaz"
                return@setOnClickListener
            }

            val todo = Todo(id = if (isEditMode) todoId else 0, name = todoName)
            viewModel.saveTodo(todo)
        }
    }
}