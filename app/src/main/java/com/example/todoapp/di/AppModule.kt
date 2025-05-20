package com.example.todoapp.di

import android.content.Context
import com.example.todoapp.data.TodoDbHelper
import com.example.todoapp.data.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDbHelper(@ApplicationContext context: Context): TodoDbHelper {
        return TodoDbHelper(context)
    }

    @Provides
    @Singleton
    fun provideTodoRepository(todoDbHelper: TodoDbHelper): TodoRepository {
        return TodoRepository(todoDbHelper)
    }
}