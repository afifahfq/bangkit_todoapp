package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.ui.list.TaskAdapter
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {
    private lateinit var viewModel: DetailTaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val edTitle = findViewById<TextInputEditText>(R.id.detail_ed_title)
        val edDescription = findViewById<TextInputEditText>(R.id.detail_ed_description)
        val edDueDate = findViewById<TextInputEditText>(R.id.detail_ed_due_date)
        val btnDelete = findViewById<Button>(R.id.btn_delete_task)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)

        viewModel.setTaskId(intent.getIntExtra(TASK_ID, 0))

        val taskObserver = Observer<Task> { aTask ->
            edTitle.setText(aTask.title)
            edDescription.setText(aTask.description)
            edDueDate.setText(DateConverter.convertMillisToString(aTask.dueDateMillis))
        }
        viewModel.task.observe(this, taskObserver)

        btnDelete.setOnClickListener {
            viewModel.deleteTask()
            viewModel.task.removeObservers(this@DetailTaskActivity)
            finish()
        }
    }
}