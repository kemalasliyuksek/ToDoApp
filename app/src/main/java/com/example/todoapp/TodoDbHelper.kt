// TodoDbHelper.kt dosyasını oluşturun
package com.example.todoapp.data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.todoapp.model.Todo

@Singleton
class TodoDbHelper @Inject constructor(
    @ApplicationContext context: Context
) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TodoApp.db"
        private const val TABLE_TODOS = "toDos"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TODOS_TABLE = ("CREATE TABLE " + TABLE_TODOS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT" + ")")
        db.execSQL(CREATE_TODOS_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TODOS")
        onCreate(db)
    }

    // Todo ekleme
    fun addTodo(todo: Todo): Long {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, todo.name)
        val id = db.insert(TABLE_TODOS, null, values)
        db.close()
        return id
    }

    // Todo'ları listeleme
    fun getAllTodos(): ArrayList<Todo> {
        val todoList = ArrayList<Todo>()
        val selectQuery = "SELECT * FROM $TABLE_TODOS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val todo = Todo(id, name)
                todoList.add(todo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todoList
    }

    // Todo arama
    fun searchTodos(query: String): ArrayList<Todo> {
        val todoList = ArrayList<Todo>()
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TODOS,
            arrayOf(COLUMN_ID, COLUMN_NAME),
            "$COLUMN_NAME LIKE ?",
            arrayOf("%$query%"),
            null,
            null,
            null
        )
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
                val todo = Todo(id, name)
                todoList.add(todo)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return todoList
    }

    // Todo güncelleme
    fun updateTodo(todo: Todo): Int {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_NAME, todo.name)
        return db.update(
            TABLE_TODOS,
            values,
            "$COLUMN_ID = ?",
            arrayOf(todo.id.toString())
        ).also { db.close() }
    }

    // Todo silme
    fun deleteTodo(todoId: Int): Int {
        val db = this.writableDatabase
        return db.delete(
            TABLE_TODOS,
            "$COLUMN_ID = ?",
            arrayOf(todoId.toString())
        ).also { db.close() }
    }

    // Todo detayını getirme
    fun getTodo(id: Int): Todo? {
        val db = this.readableDatabase
        val cursor = db.query(
            TABLE_TODOS,
            arrayOf(COLUMN_ID, COLUMN_NAME),
            "$COLUMN_ID = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )
        var todo: Todo? = null
        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME))
            todo = Todo(id, name)
        }
        cursor.close()
        db.close()
        return todo
    }
}