package my.id.fanfan.fandlystodo.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TodoDao {

    @Insert
    suspend fun saveTodo(toDo: ToDo)

    @Delete
    suspend fun deleteTodo(toDo: ToDo)

    @Update
    suspend fun updateTodo(toDo: ToDo)

    @Query("SELECT * FROM todo ORDER BY id DESC")
    fun getAllTodoList():LiveData<List<ToDo>>

    @Query("SELECT * FROM todo ORDER BY date_deadline DESC")
    fun getTodoListDeadline():LiveData<List<ToDo>>
}