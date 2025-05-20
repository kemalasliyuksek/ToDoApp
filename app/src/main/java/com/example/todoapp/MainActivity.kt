// MainActivity.kt (güncelleme)
package com.example.todoapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.adapter.TodoAdapter
import com.example.todoapp.model.Todo
import com.example.todoapp.viewmodel.TodoListViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModel: TodoListViewModel by viewModels()
    private lateinit var adapter: TodoAdapter
    private lateinit var rvTodos: RecyclerView
    private lateinit var etSearch: EditText
    private lateinit var fabAddTodo: FloatingActionButton

    companion object {
        const val REQUEST_ADD_TODO = 1
        const val REQUEST_EDIT_TODO = 2
        const val REQUEST_VIEW_TODO = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvTodos = findViewById(R.id.rvTodos)
        etSearch = findViewById(R.id.etSearch)
        fabAddTodo = findViewById(R.id.fabAddTodo)

        setupRecyclerView()
        setupSearch()
        setupFab()
        observeViewModel()

        viewModel.loadTodos()
    }

    private fun setupRecyclerView() {
        adapter = TodoAdapter(ArrayList(), { todo ->
            // Item click listener - open detail view
            val intent = Intent(this, TodoDetailActivity::class.java)
            intent.putExtra("TODO_ID", todo.id)
            startActivityForResult(intent, REQUEST_VIEW_TODO)
        }, { todoId ->
            // Delete click listener
            viewModel.deleteTodo(todoId)
        })

        rvTodos.layoutManager = LinearLayoutManager(this)
        rvTodos.adapter = adapter
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                viewModel.searchTodos(query)
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupFab() {
        fabAddTodo.setOnClickListener {
            val intent = Intent(this, TodoEditActivity::class.java)
            startActivityForResult(intent, REQUEST_ADD_TODO)
        }
    }

    private fun observeViewModel() {
        viewModel.todos.observe(this) { todos ->
            adapter.updateData(todos)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            viewModel.loadTodos()
        }
    }
}