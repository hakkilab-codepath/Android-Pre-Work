package com.example.simpletodo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText

class EditItemActivity : AppCompatActivity() {
    var position = -1
    var task = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        task = getIntent().getStringExtra("task").toString()
        position = getIntent().getIntExtra("position", -1)

        val editTextField = findViewById<EditText>(R.id.editTaskItem)
        editTextField.setText(task)

        findViewById<Button>(R.id.button).setOnClickListener {
            task = editTextField.text.toString()
            onSubmit(editTextField)
        }
    }

    fun onSubmit(v: View) {
        // closes the activity and returns edited value to first screen
        val data = Intent()
        data.putExtra("task", task)
        data.putExtra("position", position)
        this.setResult(RESULT_OK, data)
        this.finish();
    }
}