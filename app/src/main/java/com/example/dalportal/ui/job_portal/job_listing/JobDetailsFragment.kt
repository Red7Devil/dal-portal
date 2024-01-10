package com.example.dalportal.ui.job_portal.job_listing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dalportal.databinding.FragmentJobDetailsBinding
import com.example.dalportal.model.JobListing
import com.google.firebase.firestore.FirebaseFirestore
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.dalportal.R

class JobDetailsFragment : Fragment() {
    private var _binding: FragmentJobDetailsBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jobListing: JobListing? = arguments?.getParcelable("jobListing")
        displayJobDetails(jobListing)

        binding.updateButton.setOnClickListener {
            if (binding.updateButton.text.toString() == "Update") {
                enableEditing()
                binding.updateButton.text = "Save"
                binding.updateButton.setIconResource(R.drawable.ic_save) // Change to save icon
            } else {
                updateJobDetails(jobListing)
            }
        }
    }

    private fun displayJobDetails(jobListing: JobListing?) {
        jobListing?.let {
            binding.editTextJobTitle.setText(it.title)
            binding.editTextJobDescription.setText(it.description)
            binding.editTextJobLocation.setText(it.location)
            binding.editTextJobPay.setText(it.pay.toString())
            binding.editTextJobPosition.setText(it.positions.toString())
            binding.editTextJobRequirement.setText(it.requirements)
            binding.editTextJobType.setText(it.type)
        }
    }

    private fun enableEditing() {
        binding.editTextJobTitle.isEnabled = true
        binding.editTextJobDescription.isEnabled = true
        binding.editTextJobLocation.isEnabled = true
        binding.editTextJobPay.isEnabled = true
        binding.editTextJobPosition.isEnabled = true
        binding.editTextJobRequirement.isEnabled = true
        binding.editTextJobType.isEnabled = true
    }

    private fun updateJobDetails(jobListing: JobListing?) {
        jobListing?.let { job ->
            val updatedTitle = binding.editTextJobTitle.text.toString()
            val updatedDescription = binding.editTextJobDescription.text.toString()
            val updatedLocation = binding.editTextJobLocation.text.toString()
            val updatedPay = binding.editTextJobPay.text.toString().toIntOrNull() ?: 0
            val updatedPositions = binding.editTextJobPosition.text.toString().toIntOrNull() ?: 0
            val updatedRequirements = binding.editTextJobRequirement.text.toString()
            val updatedType = binding.editTextJobType.text.toString()

            val updatedJobData: Map<String, Any> = hashMapOf(
                "title" to updatedTitle,
                "description" to updatedDescription,
                "location" to updatedLocation,
                "pay" to updatedPay,
                "positions" to updatedPositions,
                "requirements" to updatedRequirements,
                "type" to updatedType
            )

            db.collection("jobPostings").document(job.id)
                .update(updatedJobData)
                .addOnSuccessListener {
                    Toast.makeText(context, "Job updated successfully", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()  // Navigate back to the previous fragment
                }
                .addOnFailureListener { e ->
                    Toast.makeText(context, "Error updating job: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
