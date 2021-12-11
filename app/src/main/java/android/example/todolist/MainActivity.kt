package android.example.todolist

import android.app.Dialog
import android.example.todolist.database.DBHandler
import android.example.todolist.model.TaskModel
import android.example.todolist.rvAdapter.ItemAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.update_task_layout.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar:Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        btnAddTask.setOnClickListener { view ->
            addTask(view)
        }

        setTaskIntoRecyclerView()
    }

    private fun setTaskIntoRecyclerView() {

        if (getItemsList().size > 0) {

            rvItemsList.visibility = View.VISIBLE
            tvNoRecordsAvailable.visibility = View.GONE

            rvItemsList.layoutManager = LinearLayoutManager(this)
            val itemAdapter = ItemAdapter(this, getItemsList())
            rvItemsList.adapter = itemAdapter
        } else {

            rvItemsList.visibility = View.GONE
            tvNoRecordsAvailable.visibility = View.VISIBLE
        }
    }

    private fun getItemsList(): ArrayList<TaskModel> {
        val databaseHandler = DBHandler(this)
        return databaseHandler.getTask()
    }

    //method for saving records in database
    private fun addTask(view: View) {
        val task = enterTaskET.text.toString()
        Log.i("Task_Added", task)
        val databaseHandler = DBHandler(this)
        if (task.isNotEmpty()) {
            val status =
                databaseHandler.addTask(TaskModel(0, task))
            if (status > -1) {
                Toast.makeText(applicationContext, "Task saved", Toast.LENGTH_LONG).show()
                enterTaskET.text.clear()
                enterTaskTIL.isErrorEnabled = false

                setTaskIntoRecyclerView()
            }
        } else {
            enterTaskTIL.isErrorEnabled = true
            enterTaskTIL.error = "Please enter your task"
        }
    }

    fun updateTask(taskModelClass: TaskModel) {
        val updateDialog = Dialog(this, R.style.Theme_Dialog)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.update_task_layout)
        updateDialog.updateTaskET.setText(taskModelClass.task)
        updateDialog.tvUpdate.setOnClickListener {

            val task = updateDialog.updateTaskET.text.toString()
            val databaseHandler = DBHandler(this)

            if (task.isNotEmpty()) {
                val status =
                    databaseHandler.updateTask(TaskModel(taskModelClass.id, task))
                if (status > -1) {
                    Toast.makeText(applicationContext, "Task Updated.", Toast.LENGTH_LONG).show()
                    setTaskIntoRecyclerView()
                    updateDialog.dismiss()
                }
            } else {
                Toast.makeText(
                    applicationContext,
                    "Task cannot be empty",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        updateDialog.tvCancel.setOnClickListener {
            updateDialog.dismiss()
        }
        updateDialog.show()
    }

    fun deleteTask(taskModelClass: TaskModel) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete Task")
        builder.setMessage("Are you sure you want to delete ${taskModelClass.task}?")
        builder.setPositiveButton("Yes") { dialogInterface, which ->
            val databaseHandler = DBHandler(this)
            val status = databaseHandler.deleteTask(TaskModel(taskModelClass.id, ""))
            if (status > -1) {
                Toast.makeText(
                    this,
                    "Task deleted successfully.",
                    Toast.LENGTH_LONG
                ).show()
                setTaskIntoRecyclerView()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No") { dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}