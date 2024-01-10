package com.example.dalportal.ui.professorportal

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.ui.professorportal.CalendarUtils.daysInMonthArray
import com.example.dalportal.ui.professorportal.CalendarUtils.monthYearFromDate
import java.time.LocalDate


class CalendarActivity : AppCompatActivity(), CalendarAdapter.OnItemListener {
    private var monthYearText: TextView? = null
    private var calendarRecyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {

        val recName = intent.getStringExtra("name")

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        initWidgets()
        CalendarUtils.selectedDate = LocalDate.now()
        setMonthView()

        supportActionBar?.title = recName
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun initWidgets() {
        calendarRecyclerView = findViewById<RecyclerView>(R.id.calendarRecyclerView)
        monthYearText = findViewById<TextView>(R.id.monthYearTV)
    }

    private fun setMonthView() {
        monthYearText!!.setText(monthYearFromDate(CalendarUtils.selectedDate))
        val daysInMonth: ArrayList<LocalDate> = daysInMonthArray()
        val calendarAdapter = CalendarAdapter(daysInMonth, this)
        val layoutManager: RecyclerView.LayoutManager = GridLayoutManager(applicationContext, 7)
        calendarRecyclerView!!.layoutManager = layoutManager
        calendarRecyclerView!!.adapter = calendarAdapter
    }

    fun previousMonthAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusMonths(1)
        setMonthView()
    }

    fun nextMonthAction(view: View?) {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusMonths(1)
        setMonthView()
    }

    override fun onItemClick(position: Int, date: LocalDate?) {
        if (date != null) {
            CalendarUtils.selectedDate = date
            setMonthView()
        }
    }

    fun weeklyAction(view: View?) {
        startActivity(Intent(this, WeekViewActivity::class.java))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Navigate back to ProfessorportalFragment when the back button is pressed
                //NavUtils.navigateUpFromSameTask(this)
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}