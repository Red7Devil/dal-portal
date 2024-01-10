package com.example.dalportal.ui.professorportal

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import java.time.LocalDate


class CalendarAdapter(
    private val days: ArrayList<LocalDate>,
    private val onItemListener: OnItemListener
) :
    RecyclerView.Adapter<CalendarViewHolder>() {

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.calendar_cell, parent, false)
        val layoutParams = view.layoutParams
        if (days.size > 15) //month view
            layoutParams.height = (parent.height * 0.166666666).toInt() else  // week view
            layoutParams.height = parent.height
        return CalendarViewHolder(view, onItemListener, days)
    }

    override fun onBindViewHolder( holder: CalendarViewHolder, position: Int) {
        val date = days[position]
        holder.dayOfMonth.setText(date.dayOfMonth.toString())
        if (date == CalendarUtils.selectedDate) holder.parentView.setBackgroundColor(Color.LTGRAY)
        if (date.month == CalendarUtils.selectedDate.getMonth()) holder.dayOfMonth.setTextColor(
            Color.BLACK
        ) else holder.dayOfMonth.setTextColor(
            Color.LTGRAY
        )
    }

    override fun getItemCount(): Int {
        return days.size
    }

    interface OnItemListener {
        fun onItemClick(position: Int, date: LocalDate?)
    }
}