package com.example.dalportal.ui.event_calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.example.dalportal.R

class EventFragment : Fragment() {
    private var monthSpinner: Spinner? = null
    private var yearSpinner: Spinner? = null
    private var selectedDateTextView: ListView? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_event, container, false)

        // Initialize views
        monthSpinner = view.findViewById(R.id.monthSpinner)
        yearSpinner = view.findViewById(R.id.yearSpinner)
        val showDateButton: Button = view.findViewById(R.id.buttonShowDate)
        selectedDateTextView = view.findViewById(R.id.listViewEvents)

        // Populate month spinner
        val months = arrayOf("January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        val monthAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, months)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner?.adapter = monthAdapter

        // Populate year spinner
        val years: List<String> = mutableListOf("2023", "2024")
        val yearAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, years)
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        yearSpinner?.adapter = yearAdapter

        // Set button click listener
        showDateButton.setOnClickListener {
            // Handle button click to show the selected date
            val selectedMonth = monthSpinner?.selectedItem.toString()
            val selectedYear = yearSpinner?.selectedItem.toString()
            val selectedDate = "$selectedMonth $selectedYear"

            // Filter events for the selected date
            val filteredEvents = EventData.events.filter { it.monthYear == selectedDate }

            // Display the events in a ListView
            val eventAdapter = ArrayAdapter(requireContext(), R.layout.list_items, filteredEvents.map { it.date+": "+it.name })
            selectedDateTextView?.adapter = eventAdapter
        }

        return view
    }
}
