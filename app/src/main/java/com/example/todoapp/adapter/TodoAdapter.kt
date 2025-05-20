package com.example.todoapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.model.Todo

class TodoAdapter(
    private val todoList: MutableList<Todo>,
    private val onItemClick: (Todo) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTodoName: TextView = itemView.findViewById(R.id.tvTodoName)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_todo, parent, false)
        return TodoViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todo = todoList[position]
        holder.tvTodoName.text = todo.name

        holder.itemView.setOnClickListener {
            onItemClick(todo)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(todo.id)
        }
    }

    override fun getItemCount(): Int = todoList.size

    fun updateData(newTodoList: List<Todo>) {
        todoList.clear()
        todoList.addAll(newTodoList)
        notifyDataSetChanged()
    }
}