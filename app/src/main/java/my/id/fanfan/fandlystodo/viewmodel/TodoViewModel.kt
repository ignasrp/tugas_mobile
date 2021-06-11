package my.id.fanfan.fandlystodo.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import my.id.fanfan.fandlystodo.database.ToDo
import my.id.fanfan.fandlystodo.database.ToDoRepository

class TodoViewModel (application: Application):AndroidViewModel(application){

    private val repository:ToDoRepository = ToDoRepository(application)
    private val allTodoList: LiveData<List<ToDo>> = repository.getAllTodoList()
    private val deadlineTodoList:LiveData<List<ToDo>> = repository.getTodoListDeadline()

    fun saveTodo(toDo: ToDo){
        repository.saveTodo(toDo)
    }

    fun updateTodo(toDo: ToDo){
        repository.updateTodo(toDo)
    }

    fun deleteTodo(toDo: ToDo){
        repository.deleteTodo(toDo)
    }

    fun getAllTodoList(): LiveData<List<ToDo>>{
        return allTodoList
    }

    fun getDeadlineTodoList(): LiveData<List<ToDo>>{
        return deadlineTodoList
    }

}