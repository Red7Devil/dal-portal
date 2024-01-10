package com.example.dalportal.ui.professorportal

import android.R
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dalportal.databinding.FragmentAssignTaskBinding
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class AssignTaskFragment : Fragment() {

    private var _binding: FragmentAssignTaskBinding? = null
    private val binding get() = _binding!!

    // Initialize Firestore
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignTaskBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initializing form fields
        val descriptionEditText = binding.editTextTaskDescription
        val deadlineEditText = binding.datePickerDeadline
        val prioritySpinner = binding.spinnerPriority

        // Initialize form fields
        val assignedToSpinner = binding.spinnerAssignedTo

        // Fetch teaching assistants and populating spinner
        fetchTeachingAssistants() { tasList ->
            val adapter = ArrayAdapter(requireContext(), R.layout.simple_spinner_item, tasList)
            adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
            assignedToSpinner.adapter = adapter
        }

        val assignButton = binding.assignButton
        assignButton.setOnClickListener {

            val description = descriptionEditText.text.toString()
            val priority = prioritySpinner.selectedItem.toString()
            val assignedTo = assignedToSpinner.selectedItem.toString()  // TA's user ID

            // Convert DatePicker date to a readable format
            val day = deadlineEditText.dayOfMonth
            val month = deadlineEditText.month
            val year = deadlineEditText.year

            val calendar = Calendar.getInstance()
            calendar.set(year, month, day)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val formattedDeadline = sdf.format(calendar.time)

            val task = mapOf(
                "description" to description,
                "deadline" to formattedDeadline,
                "priority" to priority,
                "assignedTo" to assignedTo,
                "status" to "Not Started",
                "timestamp" to FieldValue.serverTimestamp()
            )

            // Add the task to Firestore
            db.collection("TA")
                .add(task)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Task assigned successfully", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error assigning task", Toast.LENGTH_SHORT).show()
                }

        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

        private fun fetchTeachingAssistants(callback: (List<String>) -> Unit) {
            db.collection("users")

                .whereEqualTo("role", "TA")
                .get()
                .addOnSuccessListener { result ->
                    if (result.documents.isNotEmpty()) {
                       // val tasList = result.documents[0].get("TA") as? List<String> ?: emptyList()
                        val tasList = result.documents.map { document ->
                            document.getString("name") ?: ""
                        }
                        callback(tasList)
                    } else {

                        Toast.makeText(requireContext(), "No data found for the course", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(requireContext(), "Error fetching teaching assistants", Toast.LENGTH_SHORT).show()
                }
        }
}
