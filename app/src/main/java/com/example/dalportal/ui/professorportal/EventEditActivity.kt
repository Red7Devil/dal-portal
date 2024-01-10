package com.example.dalportal.ui.professorportal

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.dalportal.R
import com.example.dalportal.ui.professorportal.CalendarUtils.formattedDate
import java.time.LocalTime


class EventEditActivity : AppCompatActivity() {
    private var eventNameET: EditText? = null
    private var eventDateTV: TextView? = null
    private var eventTimeTV: TextView? = null
    private var eventTimePicker: TimePicker? = null
    private lateinit var time: LocalTime

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event_edit)
        initWidgets()
        val recName = intent.getStringExtra("name")

        time = LocalTime.now()
        eventDateTV!!.text = "Date: " + formattedDate(CalendarUtils.selectedDate)
      //  eventTimeTV!!.text = "Time: " + formattedTime(time)
        eventTimePicker!!.setIs24HourView(true) // Set 24-hour format
        eventTimePicker!!.hour = time.hour
        eventTimePicker!!.minute = time.minute

        supportActionBar?.title = recName
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun initWidgets() {
        eventNameET = findViewById<EditText>(R.id.eventNameET)
        eventDateTV = findViewById<TextView>(R.id.eventDateTV)
        eventTimeTV = findViewById<TextView>(R.id.eventTimeTV)
        eventTimePicker = findViewById<TimePicker>(R.id.eventTimePicker)
    }

    fun saveEventAction(view: View?) {
        val eventName = eventNameET!!.text.toString()

        // Get the selected time from TimePicker
        val hour = eventTimePicker!!.hour
        val minute = eventTimePicker!!.minute
        time = LocalTime.of(hour, minute)


        val newEvent = Event(
            eventName, CalendarUtils.selectedDate,
            time!!
        )
        Event.eventsList.add(newEvent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Navigate back to the previous activity when the back button is pressed
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}