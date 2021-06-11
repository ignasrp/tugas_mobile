package my.id.fanfan.fandlystodo.database

import android.app.Application
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ToDoRepository(application: Application) {

    private val todoDao:TodoDao
    private val allTodos:LiveData<List<ToDo>>
    private val allTodos1:LiveData<List<ToDo>>

    init {
        val database = TodoDatabase.getInstance(application.applicationContext)
        todoDao = database!!.todoDao()
        allTodos = todoDao.getAllTodoList()
        allTodos1 = todoDao.getTodoListDeadline()

    }

    fun saveTodo(toDo: ToDo)= runBlocking {
        this.launch(Dispatchers.IO){
            todoDao.saveTodo(toDo)
        }
    }

    fun updateTodo(toDo: ToDo) = runBlocking{
        this.launch(Dispatchers.IO){
            todoDao.updateTodo(toDo)
        }
    }

    fun deleteTodo(toDo: ToDo){
        runBlocking {
            this.launch(Dispatchers.IO){
                todoDao.deleteTodo(toDo)
            }
        }
    }

    fun getAllTodoList():LiveData<List<ToDo>>{
        return allTodos
    }

    fun getTodoListDeadline():LiveData<List<ToDo>>{
        return allTodos1
    }

}