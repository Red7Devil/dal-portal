package com.example.dalportal.ui.professorportal

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.dalportal.R
import com.example.dalportal.ui.professorportal.CalendarUtils.monthDayFromDate
import com.example.dalportal.ui.professorportal.CalendarUtils.selectedDate
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.Locale


class DailyCalendarActivity : AppCompatActivity() {
    private var monthDayText: TextView? = null
    private var dayOfWeekTV: TextView? = null
    private var hourListView: ListView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        val recName = intent.getStringExtra("name")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_daily_calendar)
        initWidgets()

        supportActionBar?.title = recName
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

    }

    private fun initWidgets() {
        monthDayText = findViewById<TextView>(R.id.monthDayText)
        dayOfWeekTV = findViewById<TextView>(R.id.dayOfWeekTV)
        hourListView = findViewById<ListView>(R.id.hourListView)
    }

    override fun onResume() {
        super.onResume()
        setDayView()
    }

    private fun setDayView() {
        monthDayText!!.text = monthDayFromDate(selectedDate)
        val dayOfWeek: String =
            selectedDate.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.getDefault())
        dayOfWeekTV!!.text = dayOfWeek
        setHourAdapter()
    }

    private fun setHourAdapter() {
        val hourAdapter = HourAdapter(applicationContext, hourEventList())
        hourListView!!.adapter = hourAdapter
    }

    private fun hourEventList(): ArrayList<HourEvent> {
        val list: ArrayList<HourEvent> = ArrayList<HourEvent>()
        for (hour in 0..23) {
            val time = LocalTime.of(hour, 0)
            val events = Event.eventsForDateAndTime(selectedDate, time)
            val hourEvent = HourEvent(time, events)
            list.add(hourEvent)
        }
        return list
    }

    fun previousDayAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusDays(1)
        setDayView()
    }

    fun nextDayAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusDays(1)
        setDayView()
    }

    fun newEventAction(view: View?) {
        startActivity(Intent(this, EventEditActivity::class.java))
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