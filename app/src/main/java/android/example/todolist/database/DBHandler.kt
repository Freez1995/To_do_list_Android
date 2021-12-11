package android.example.todolist.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.example.todolist.model.TaskModel

class DBHandler(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "TaskListDatabase"
        private const val TABLE_TASK= "TaskListTable"

        private val KEY_ID = "_id"
        private val KEY_TASK = "task"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TASK_TABLE = ("CREATE TABLE " + TABLE_TASK + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TASK + " TEXT" + ")")
        db?.execSQL(CREATE_TASK_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_TASK")
        onCreate(db)
    }


    fun addTask(task: TaskModel): Long{
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TASK, task.task)
        val success = db.insert(TABLE_TASK, null, contentValues)
        db.close()
        return success
    }

    fun getTask(): ArrayList<TaskModel>{
        val taskList: ArrayList<TaskModel> = ArrayList<TaskModel>()

        val selectQuery = "SELECT * FROM $TABLE_TASK"
        val db = this.readableDatabase
        var cursor: Cursor? = null

        try {
            cursor = db.rawQuery(selectQuery, null)

        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var task: String

        if (cursor.moveToFirst()) {
            do {
                id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID))
                task = cursor.getString(cursor.getColumnIndexOrThrow(KEY_TASK))

                val taskItem = TaskModel(id=id, task = task)
                taskList.add(taskItem)
            }while (cursor.moveToNext())
        }
        return taskList
    }

    fun updateTask(task: TaskModel): Int{
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TASK, task.task)
        val success = db.update(TABLE_TASK, contentValues, KEY_ID + "=" + task.id, null)
        db.close()
        return success
    }

    fun deleteTask(task: TaskModel): Int{
        val db = writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, task.id)
        val success = db.delete(TABLE_TASK, KEY_ID + "=" + task.id, null)
        db.close()
        return success
    }

}