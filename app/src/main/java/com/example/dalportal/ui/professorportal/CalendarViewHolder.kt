package com.example.dalportal.ui.professorportal

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.ui.professorportal.CalendarAdapter.OnItemListener
import java.time.LocalDate


class CalendarViewHolder(
    itemView: View,
    private val onItemListener: OnItemListener,
    days: ArrayList<LocalDate>
) :
    RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val days: ArrayList<LocalDate>
    val parentView: View
    val dayOfMonth: TextView

    init {
        parentView = itemView.findViewById<View>(R.id.parentView)
        dayOfMonth = itemView.findViewById<TextView>(R.id.cellDayText)
        itemView.setOnClickListener(this)
        this.days = days
    }

    override fun onClick(view: View) {
        onItemListener.onItemClick(adapterPosition, days[adapterPosition])
    }
}