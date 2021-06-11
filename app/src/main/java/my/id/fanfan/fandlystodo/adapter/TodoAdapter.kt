package my.id.fanfan.fandlystodo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.appcompat.view.menu.MenuView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list.view.*
import my.id.fanfan.fandlystodo.R
import my.id.fanfan.fandlystodo.database.ToDo
import java.text.SimpleDateFormat
import java.util.*
import java.util.Arrays.sort
import java.util.Collections.sort

class TodoAdapter(todoEvents:TodoEvents): RecyclerView.Adapter<TodoAdapter.ViewHolder>(),Filterable {
    private var todoList : List<ToDo> = arrayListOf()
    private var filteredTodoList: List<ToDo> = arrayListOf()
    private val listener: TodoEvents = todoEvents



    class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        fun bind(toDo: ToDo,listener:TodoEvents){
            itemView.tv_item_title.text = toDo.title
            itemView.tv_item_content.text = toDo.content
            itemView.tv_date_create.text = toDo.dateCreate
            itemView.tv_date_deadline.text = toDo.dateDeadline

            itemView.iv_item_delete.setOnClickListener {
                listener.onDeleteClicked(toDo)
            }

            itemView.setOnClickListener {
                listener.onViewClicked(toDo)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount():Int= filteredTodoList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(filteredTodoList[position], listener)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(p0: CharSequence?): FilterResults {
                val charString = p0.toString()
                filteredTodoList = if (charString.isEmpty()) {
                    todoList
                } else {
                    val filteredList = arrayListOf<ToDo>()
                    for (row in todoList) {
                        if (row.title.toLowerCase().contains(charString.toLowerCase())
                            || row.content.contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = filteredTodoList
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredTodoList = p1?.values as List<ToDo>
                notifyDataSetChanged()
            }

        }
    }

    fun setAllTodoItems(todoItems: List<ToDo>){
        this.todoList = todoItems
        this.filteredTodoList = todoItems
        notifyDataSetChanged()
    }


    interface TodoEvents{
        fun onDeleteClicked(toDo: ToDo)
        fun onViewClicked(toDo: ToDo)
    }


}

