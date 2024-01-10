package com.example.dalportal.ui.professorportal

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColor
import androidx.core.graphics.toColorInt
import com.example.dalportal.R
import com.example.dalportal.databinding.FragmentProfessorOverviewDashboardBinding
import com.example.dalportal.model.DashboardMetrics
import com.example.dalportal.model.TA_task
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Locale

class ProfessorOverviewDashboardFragment : Fragment() {

    private lateinit var barChart: BarChart
    private lateinit var pieChart: PieChart

    private var _binding: FragmentProfessorOverviewDashboardBinding? = null
    private val binding get() = _binding!!
    // Initialize Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfessorOverviewDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val view = inflater.inflate(R.layout.fragment_professor_overview_dashboard, container, false)

        barChart = view.findViewById(R.id.barChart)
        pieChart = view.findViewById(R.id.pieChart)

        // Initialize and fetch data from Firestore
        fetchTasksFromFirestore()

        return view
    }



    private fun fetchTasksFromFirestore() {
        val tasks = mutableListOf<TA_task>()


        db.collection("TA")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    // Check if the document is not empty
                    if (document.exists()) {
                        val taskId = document.id
                        val assignedBy = document.getString("assignedBy") ?: ""
                        val assignedTo = document.getString("assignedTo") ?: ""
                        val description = document.getString("description") ?: ""
                        val priority = document.getString("priority") ?: ""
                        val status = document.getString("status") ?: ""

                        // Retrieve the 'deadline' field
                        val deadlineField = document["deadline"]

                        // Check the type of 'deadline' field
                        val deadline = when (deadlineField) {
                            is String -> parseDeadlineString(deadlineField) // Parse the String to Timestamp
                            is Timestamp -> deadlineField // Use directly if already a Timestamp
                            else -> Timestamp.now() // Use a default Timestamp value
                        }


                        // Create a TA_task object and add it to the list
                        val task = TA_task(taskId, description, deadline as Timestamp, priority, assignedTo, assignedBy, status)
                        tasks.add(task)
                    } else {
                        // Handle the case where the document is empty
                        Log.d("Firestore", "Document is empty")
                        System.err.println("Error message ahar : ")
                    }
                }

                // Update UI with the fetched data
                updateDashboardUI(tasks)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error getting documents: ", exception)
                System.err.println("Error message : " + exception.message)
            }
    }


    private fun updateDashboardUI(tasks: List<TA_task>) {

        // Calculate metrics based on tasks
        val statusMetrics = calculateStatusMetrics(tasks)
        val priorityMetrics = calculatePriorityMetrics(tasks)

        // Update UI elements with the calculated metrics
        displayStatusMetricsOnPieChart(statusMetrics)
        displayPriorityMetricsOnBarChart(priorityMetrics)
    }



    private fun calculateStatusMetrics(tasks: List<TA_task>): Map<String, Int> {
        // Calculate metrics based on status (e.g., count of completed, pending, started, aborted)
        return tasks.groupingBy { it.status }.eachCount()
    }

    private fun calculatePriorityMetrics(tasks: List<TA_task>): Map<String, Int> {
        // Calculate metrics based on priority (e.g., count of low, medium, high)
        return tasks.groupingBy { it.priority }.eachCount()
    }

    private fun displayStatusMetricsOnPieChart(statusMetrics: Map<String, Int>) {
        val entries = statusMetrics.map { (status, count) -> PieEntry(count.toFloat(), status) }

        val pieDataSet = PieDataSet(entries, "Task Status")
        pieDataSet.colors = ColorTemplate.JOYFUL_COLORS.toList()

        val pieData = PieData(pieDataSet)
        pieChart.data = pieData
        pieChart.description.text = "Task Status"
        pieChart.animateY(1500)

        // Customize chart appearance
        pieChart.legend.orientation = Legend.LegendOrientation.HORIZONTAL
        pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        pieChart.setEntryLabelColor(android.R.color.holo_red_dark) // Set label text color

        // Set value text color
        pieData.setValueTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
        pieData.setValueTextSize(12f) // Adjust text size as needed
        pieChart.description.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))


    }

    private fun displayPriorityMetricsOnBarChart(priorityMetrics: Map<String, Int>) {
        val entries = priorityMetrics.map { (priority, count) -> BarEntry(1f, count.toFloat()) }

        val barDataSet = BarDataSet(entries, "Task Priority")
        barDataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()


        val barData = BarData(barDataSet)
        barChart.data = barData
        barChart.description.text = "Task Priority"
        barChart.setFitBars(true)
        barChart.animateY(1500)

        // Customize chart appearance
        barChart.xAxis.labelCount = entries.size
        barChart.xAxis.textColor = ContextCompat.getColor(requireContext(), R.color.blue)

        barChart.xAxis.setDrawLabels(true)
        barChart.xAxis.setDrawGridLines(false)

        // Customize chart appearance
        barChart.xAxis.labelCount = entries.size
        barChart.xAxis.setDrawLabels(true)
        barChart.xAxis.setDrawGridLines(false)

        // Set value text color
        barData.setValueTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
        barData.setValueTextSize(12f) // Adjust text size as needed
        barChart.description.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun parseDeadlineString(deadlineString: String): Timestamp {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        try {
            val date = dateFormat.parse(deadlineString)
            return Timestamp(date!!)
        } catch (e: Exception) {
            Log.e("Firestore", "Error parsing deadline string: $deadlineString", e)
        }

        // Return a default Timestamp if parsing fails
        return Timestamp.now()
    }

}