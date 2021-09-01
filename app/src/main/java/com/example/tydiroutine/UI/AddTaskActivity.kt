package com.example.tydiroutine.UI

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tydiroutine.databinding.ActivityAddTaskBinding
import com.example.tydiroutine.datasource.TaskDataSource
import com.example.tydiroutine.extencions.format
import com.example.tydiroutine.extencions.text
import com.example.tydiroutine.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class AddTaskActivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            TaskDataSource.findById(taskId)?.let {
                binding.tiTitle.text = it.title
                binding.tiData.text = it.date
                binding.tiTime.text = it.hour
            }
        }

        insertListenners()
    }

    private fun insertListenners() {
        binding.tiData.editText?.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().build()
            datePicker.addOnPositiveButtonClickListener {
                val timeZone = TimeZone.getDefault()
                val offset =  timeZone.getOffset(Date().time) * -1
                binding.tiData.text = Date(it + offset).format()
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER_TAG")
        }

        binding.tiTime.editText?.setOnClickListener {
            val timePicker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .build()
            timePicker.addOnPositiveButtonClickListener {
               val minute =  if (timePicker.minute in 0..9) "0${timePicker.minute}" else timePicker.minute
                val hour = if (timePicker.hour in 0..9) "0${timePicker.hour}" else timePicker.hour
                binding.tiTime.text ="$hour:$minute"
            }

            timePicker.show(supportFragmentManager, null)

        }

        binding.buttomCancel.setOnClickListener {
            finish()
        }

        binding.buttonCreate.setOnClickListener {
            val task = Task(
                title = binding.tiTitle.text ,
                hour = binding.tiTime.text ,
                date = binding.tiData.text,
                id = intent.getIntExtra(TASK_ID, 0)
            )
            TaskDataSource.insertTask(task)
            setResult(Activity.RESULT_OK)
            finish()
        }

    }

    companion object {
        const val TASK_ID = "task_id"
    }

}