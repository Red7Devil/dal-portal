package com.example.dalportal.ui.professorportal

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.dalportal.databinding.FragmentProfDashboardBinding
import com.google.firebase.firestore.FirebaseFirestore

class ProfDashboardFragment : Fragment() {

    private var _binding: FragmentProfDashboardBinding? = null
    private val binding get() = _binding!!

    // Initialize Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize form fields
        val taskDescriptionTextView: TextView = binding.taskdesc
        val taskIndicator = binding.taskStatus

        // Fetch task data from Firestore
        fetchTaskData()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun fetchTaskData() {
        db.collection("TA")
            .get()
            .addOnSuccessListener { result ->
                // List to store tasks
                val tasks = mutableListOf<String>()

                // Loop through documents and add task details to the list
                for (document in result) {
                    val description = document.getString("description")
                    val status = document.getString("status")

                    // Example: Display task details in TextView
                    val taskDetails = "Description: $description, Status: $status"
                    tasks.add(taskDetails)
                }

                // Display tasks in TextView or update UI accordingly
                updateUI(tasks)
            }
            .addOnFailureListener { exception ->
                System.err.println(exception)
            }
    }

    private fun updateUI(tasks: List<String>) {
        val taskDescriptionTextView: TextView = binding.taskdesc
        taskDescriptionTextView.text = tasks.joinToString("\n")
    }
}
