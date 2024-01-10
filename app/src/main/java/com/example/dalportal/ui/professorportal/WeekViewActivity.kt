package com.example.dalportal.ui.professorportal

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.ui.professorportal.CalendarAdapter.OnItemListener
import com.example.dalportal.ui.professorportal.CalendarUtils.daysInWeekArray
import com.example.dalportal.ui.professorportal.CalendarUtils.monthYearFromDate
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate


class WeekViewActivity : AppCompatActivity(), OnItemListener {
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    private var eventListView: ListView? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        val recName = intent.getStringExtra("name")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_week_view)
        initWidgets()
        setWeekView()

        supportActionBar?.title = recName
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        monthYearText = findViewById<TextView>(R.id.monthYearTV)
        eventListView = findViewById<ListView>(R.id.eventListView)
    }

    private fun setWeekView() {
        monthYearText!!.text = monthYearFromDate(CalendarUtils.selectedDate)
        val days: ArrayList<LocalDate> = daysInWeekArray(CalendarUtils.selectedDate)
        val calendarAdapter = CalendarAdapter(days, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
        setEventAdpater()
    }

    fun previousWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1)
        setWeekView()
    }

    fun nextWeekAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1)
        setWeekView()
    }

    override fun onItemClick(position: Int, date: LocalDate?) {
        CalendarUtils.selectedDate = date!!
        setWeekView()
    }

    override fun onResume() {
        super.onResume()
        setEventAdpater()
    }

    private fun setEventAdpater() {
        val dailyEvents: ArrayList<Event> = Event.eventsForDate(CalendarUtils.selectedDate)
        val eventAdapter = EventAdapter(applicationContext, dailyEvents)
        eventListView!!.adapter = eventAdapter
    }

    fun newEventAction(view: View?) {
        startActivity(Intent(this, EventEditActivity::class.java))
    }

    fun dailyAction(view: View?) {
        startActivity(Intent(this, DailyCalendarActivity::class.java))
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