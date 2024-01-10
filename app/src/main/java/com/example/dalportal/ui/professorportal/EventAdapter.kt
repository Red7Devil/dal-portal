package com.example.dalportal.ui.professorportal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.dalportal.R
import com.example.dalportal.ui.professorportal.CalendarUtils.formattedTime


class EventAdapter( context: Context?, events: kotlin.collections.List<Event?>?) :
    ArrayAdapter<Event?>(context!!, 0, events!!) {

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val event = getItem(position)
        if (convertView == null) convertView =
            LayoutInflater.from(context).inflate(R.layout.event_cell, parent, false)
        val eventCellTV = convertView!!.findViewById<TextView>(R.id.eventCellTV)
        val eventTitle = event!!.name + " " + formattedTime(
            event.time
        )
        eventCellTV.text = eventTitle
        return convertView
    }
}