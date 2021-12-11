package android.example.todolist.rvAdapter

import android.content.Context
import android.example.todolist.MainActivity
import android.example.todolist.R
import android.example.todolist.model.TaskModel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recycler_view_row.view.*

class ItemAdapter(private val context: Context, private val items: ArrayList<TaskModel>):
    RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate
                (R.layout.recycler_view_row,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.taskTV.text = item.task
//        holder.rvCardView.setCardBackgroundColor(
//            ContextCompat.getColor(
//                context,
//                R.color.lightGray
//            )
//        )

        holder.editTaskIV.setOnClickListener {

            if (context is MainActivity) {
                context.updateTask(item)
            }
        }

        holder.deleteTaskIV.setOnClickListener {

            if (context is MainActivity) {
                context.deleteTask(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val rvCardView: CardView = view.rvCardView
        val taskTV: TextView = view.taskTV
        val editTaskIV: ImageView = view.editTaskIV
        val deleteTaskIV: ImageView = view.deleteTaskIV
    }


}