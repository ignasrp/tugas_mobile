package my.id.fanfan.fandlystodo.activity

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_create_todo.*
import my.id.fanfan.fandlystodo.Constants
import my.id.fanfan.fandlystodo.R
import my.id.fanfan.fandlystodo.database.ToDo
import java.text.SimpleDateFormat
import java.util.*


class CreateTodoActivity : AppCompatActivity() {

    var toDo: ToDo? = null
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_todo)

        val intent = intent
        if (intent != null && intent.hasExtra((Constants.INTENT_OBJECT))) {
            val toDo: ToDo = intent.getParcelableExtra(Constants.INTENT_OBJECT)
            this.toDo = toDo
            prePopulateData(toDo)
        }

        title =
            if (toDo != null) getString(R.string.viewOrEditTodo) else getString(R.string.createTodo)

        edt_date_pick.setOnClickListener {
            val dpd = DatePickerDialog(
                this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    val cal = Calendar.getInstance()
                    c.set(Calendar.YEAR, year)
                    c.set(Calendar.MONTH, month)
                    c.set(Calendar.DAY_OF_MONTH, day)
                    edt_date_pick.text =
                        SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(c.time)
                            .toEditable()
                },
                year,
                month,
                day
            )
            dpd.show()
        }

        edt_time_pick.setOnClickListener {
            val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                c.set(Calendar.HOUR_OF_DAY, hour)
                c.set(Calendar.MINUTE, minute)
                edt_time_pick.text = SimpleDateFormat("HH:mm").format(c.time).toEditable()
            }
            TimePickerDialog(
                this,
                timeSetListener,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
            ).show()
        }
    }

    private fun prePopulateData(toDo: ToDo) {
        edt_todo_title.setText(toDo.title)
        edt_todo_content.setText(toDo.content)
        edt_date_pick.setText(toDo.dateDeadline?.substring(0, toDo.dateDeadline.indexOf(" ")))
        edt_time_pick.setText(toDo.dateDeadline?.substring(toDo.dateDeadline.indexOf(" ") + 1))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflate = menuInflater
        menuInflate.inflate(R.menu.menu_save, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.save_todo -> {
                saveTodo()
            }
        }
        return true
    }

    private fun saveTodo() {
        if (validateFields()) {
            val id = if (toDo != null) toDo?.id else null
            val todo = ToDo(
                id = id,
                title = edt_todo_title.text.toString(),
                content = edt_todo_content.text.toString(),
                dateCreate = getCurrentDate(),
                dateDeadline = edt_date_pick.text.toString() + " " + edt_time_pick.text.toString()
            )
            val intent = Intent()
            intent.putExtra(Constants.INTENT_OBJECT, todo)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    private fun validateFields(): Boolean {
        if (edt_todo_title.text.isEmpty()) {
            til_todo_title.error = "Please Enter Title"
            edt_todo_title.requestFocus()
            return false
        }
        if (edt_todo_content.text.isEmpty()) {
            til_todo_content.error = "Please Enter Content"
            edt_todo_content.requestFocus()
            return false
        }
        return true
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

}
