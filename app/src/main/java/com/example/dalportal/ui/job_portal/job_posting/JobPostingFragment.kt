package com.example.dalportal.ui.job_portal.job_posting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.dalportal.R
import com.example.dalportal.databinding.FragmentJobPostingBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.firebase.firestore.FirebaseFirestore
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject
import java.io.IOException

class JobPostingFragment : Fragment() {
    private var _binding: FragmentJobPostingBinding? = null
    private val binding get() = _binding!!
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobPostingBinding.inflate(inflater, container, false)
        setupButtonClickListener()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupJobTypeDropdown()
    }

    private fun setupJobTypeDropdown() {
        val items = resources.getStringArray(R.array.job_types)
        val adapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, items)
        val jobTypeDropdown = view?.findViewById<MaterialAutoCompleteTextView>(R.id.jobType)
        jobTypeDropdown?.setAdapter(adapter)
    }

    private fun setupButtonClickListener() {
        binding.submitJobPosting.setOnClickListener {
            if (validateForm()) {
                saveJobPostingToFirestore()
            }
        }
    }

    private fun validateForm(): Boolean {
        // Local variables for EditText content
        val jobTitleText = binding.jobTitle.text
        val jobDescriptionText = binding.jobDescription.text
        val jobTypeText = binding.jobType.text
        val numPositionsText = binding.numPositions.text
        val jobLocationText = binding.jobLocation.text
        val rateOfPayText = binding.rateOfPay.text
        val jobRequirementsText = binding.jobRequirements.text

        // Validation checks
        if (jobTitleText.isNullOrEmpty() || jobDescriptionText.isNullOrEmpty() || jobDescriptionText.length < 100 ||
            jobTypeText.isNullOrEmpty() || numPositionsText.isNullOrEmpty() ||
            jobLocationText.isNullOrEmpty() || rateOfPayText.isNullOrEmpty() ||
            jobRequirementsText.isNullOrEmpty() || jobRequirementsText.length < 100) {
            Toast.makeText(requireContext(), "Please fill in all the fields correctly", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

    private fun saveJobPostingToFirestore() {
        // Convert EditText content to String
        val jobTitle = binding.jobTitle.text.toString()
        val jobDescription = binding.jobDescription.text.toString()
        val jobType = binding.jobType.text.toString()
        val numPositions = binding.numPositions.text.toString().toIntOrNull() ?: 0
        val jobLocation = binding.jobLocation.text.toString()
        val rateOfPay = binding.rateOfPay.text.toString().toIntOrNull() ?: 0
        val jobRequirements = binding.jobRequirements.text.toString()

        fetchTags("$jobDescription $jobRequirements") { tags ->
            val jobData = hashMapOf(
                "title" to jobTitle,
                "description" to jobDescription,
                "type" to jobType,
                "positions" to numPositions,
                "location" to jobLocation,
                "pay" to rateOfPay,
                "requirements" to jobRequirements,
                "tags" to tags
            )

            db.collection("jobPostings")
                .add(jobData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Job posting successfully added!", Toast.LENGTH_SHORT).show()
                    clearForm()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error adding job posting: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
    private fun fetchTags(jobDescription: String, onComplete: (List<String>) -> Unit) {
        val client = OkHttpClient()
        val requestJson = JSONObject().apply {
            put("questionText", jobDescription)
        }

        val requestBody = RequestBody.create(
            "application/json; charset=utf-8".toMediaTypeOrNull(),
            requestJson.toString()
        )

        val request = Request.Builder()
            .url("https://auto-tagging-ynig4ve4cq-ue.a.run.app")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                activity?.runOnUiThread {
                    Toast.makeText(requireContext(), "Error making call: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseJson = JSONObject(response.body?.string() ?: "")
                    val tags = responseJson.getJSONArray("tags")
                    val tagList = ArrayList<String>()
                    for (i in 0 until tags.length()) {
                        tagList.add(tags.getString(i))
                    }
                    activity?.runOnUiThread {
                        onComplete(tagList)
                    }
                } else {
                    activity?.runOnUiThread {
                        Toast.makeText(requireContext(), "Error creating tags", Toast.LENGTH_LONG).show()
                    }
                }
            }
        })
    }

    private fun clearForm() {
        binding.jobTitle.setText("")
        binding.jobDescription.setText("")
        binding.jobType.setText("") // Adjusted for MaterialAutoCompleteTextView
        binding.numPositions.setText("")
        binding.jobLocation.setText("")
        binding.rateOfPay.setText("")
        binding.jobRequirements.setText("")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
