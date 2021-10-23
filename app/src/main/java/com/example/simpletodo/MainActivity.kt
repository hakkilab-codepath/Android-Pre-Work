package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter
    val REQUEST_CODE = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // remove a task on a long click
        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongClicked(position: Int) {
                listOfTasks.removeAt(position)
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }

        // edit a task on click
        val onClickListener = object : TaskItemAdapter.OnClickListener {
            override fun onItemClicked(position: Int) {
                // intent for editing activity
                val i = Intent(this@MainActivity, EditItemActivity::class.java)
                val task = listOfTasks.get(position)
                i.putExtra("task", task)
                i.putExtra("position", position)
                startActivityForResult(i, REQUEST_CODE) // brings up the edit activity
            }
        }

        loadItems()

        // Lookup the recyclerview in activity layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the tasks
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener, onClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        // add user inputted task when button is pressed
        findViewById<Button>(R.id.button).setOnClickListener {
            val userInputtedTask = inputTextField.text.toString()
            listOfTasks.add(userInputtedTask)
            adapter.notifyItemInserted(listOfTasks.size - 1)
            // clear text field
            inputTextField.setText("")
            // save tasks
            saveItems()
        }
    }

    // Save/load tasks entered by the user to/from a file (each line is a task)

    // Access data file
    fun getDataFile() : File {
        return File(filesDir, "data.txt")
    }

    // Load tasks from data file
    fun loadItems() {
        try {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // Save tasks to data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        } catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }

    // handle results from another activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            val task = data?.getExtras()?.getString("task")
            val position = data?.getExtras()?.getInt("position")
            if (position != null && task!= null) {
                listOfTasks.set(position, task)
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }
    }
}