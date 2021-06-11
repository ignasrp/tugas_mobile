

package my.id.fanfan.fandlystodo.activity

import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import my.id.fanfan.fandlystodo.Constants
import my.id.fanfan.fandlystodo.R
import my.id.fanfan.fandlystodo.adapter.TodoAdapter
import my.id.fanfan.fandlystodo.database.ToDo
import my.id.fanfan.fandlystodo.viewmodel.TodoViewModel

class MainActivity : AppCompatActivity(), TodoAdapter.TodoEvents {
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var searchView: SearchView
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //setup recycle view
        rv_todo_list.layoutManager = LinearLayoutManager(this)
        todoAdapter = TodoAdapter(this)
        rv_todo_list.adapter = todoAdapter

        //set ViewModel + Live Data
        todoViewModel = ViewModelProviders.of(this).get(TodoViewModel::class.java)
        getAllData()


        btn_sortbydeadline.setOnClickListener {
            todoViewModel.getDeadlineTodoList().observe(this, Observer {
                todoAdapter.setAllTodoItems(it)
            })
        }

        btn_sortbycreate.setOnClickListener {
            getAllData()
        }


        //floating button listener
        fltbtn_new_todo.setOnClickListener {
            resetSearchView()
            val intent = Intent(this@MainActivity,CreateTodoActivity::class.java)
            startActivityForResult(intent,Constants.INTENT_CREATE_TODO)
        }
    }

    override fun onDeleteClicked(toDo: ToDo) {
        todoViewModel.deleteTodo(toDo)
    }

    override fun onViewClicked(toDo: ToDo) {
        resetSearchView()
        val intent = Intent(this@MainActivity, CreateTodoActivity::class.java)
        intent.putExtra(Constants.INTENT_OBJECT,toDo)
        startActivityForResult(intent,Constants.INTENT_UPDATE_TODO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val todoRecord = data?.getParcelableExtra<ToDo>(Constants.INTENT_OBJECT)!!
            when (requestCode) {
                Constants.INTENT_CREATE_TODO -> {
                    todoViewModel.saveTodo(todoRecord)
                }
                Constants.INTENT_UPDATE_TODO -> {
                    todoViewModel.updateTodo(todoRecord)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView = menu?.findItem(R.id.search_todo)
            ?.actionView as SearchView
        searchView.setSearchableInfo(searchManager
            .getSearchableInfo(componentName))
        searchView.maxWidth = Integer.MAX_VALUE
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                todoAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                todoAdapter.filter.filter(newText)
                return false
            }

        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.search_todo -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        resetSearchView()
        super.onBackPressed()
    }

    private fun resetSearchView() {
        if (!searchView.isIconified) {
            searchView.isIconified = true
            return
        }
    }

    private fun getAllData(){
        todoViewModel.getAllTodoList().observe(this, Observer {
            todoAdapter.setAllTodoItems(it)
        })
    }
}
