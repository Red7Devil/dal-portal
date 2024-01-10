package com.example.dalportal.ui.job_portal.job_listing

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dalportal.R
import com.example.dalportal.databinding.FragmentJobListingBinding
import com.example.dalportal.model.JobListing
import com.example.dalportal.util.UserData
import com.google.firebase.firestore.FirebaseFirestore

class JobListingFragment : Fragment() {
    private var _binding: FragmentJobListingBinding? = null
    private val binding get() = _binding!!
    private lateinit var jobListingsFull: List<JobListing> // Full list of job postings
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentJobListingBinding.inflate(inflater, container, false)
        setupRecyclerView()
        loadJobPostings()
        setupSearchView()
        binding.addJobButton.isVisible = UserData.role == "Professor" || UserData.role == "admin"
        binding.addJobButton.setOnClickListener {
            findNavController().navigate(R.id.action_jobListingFragment_to_jobPostingFragment)
        }
        return binding.root
    }


    private fun setupRecyclerView() {
        binding.recyclerViewJobListings.layoutManager = LinearLayoutManager(context)
        binding.recyclerViewJobListings.adapter =
            JobListingAdapter(mutableListOf(), requireContext()) { jobListing ->
                val bundle = Bundle().apply {
                    putParcelable("jobListing", jobListing)
                }
                findNavController().navigate(
                    R.id.action_jobListingFragment_to_jobDetailsFragment,
                    bundle
                )

            }
    }

    private fun loadJobPostings() {
        db.collection("jobPostings").get()
            .addOnSuccessListener { documents ->
                jobListingsFull = documents.mapNotNull { doc ->
                    val tagsList = doc.get("tags") as? List<String> ?: listOf() // Fetch tags
                    JobListing(
                        id = doc.id,
                        title = doc.getString("title") ?: "",
                        description = doc.getString("description") ?: "",
                        location = doc.getString("location") ?: "",
                        pay = doc.getLong("pay")?.toInt() ?: 0,
                        positions = doc.getLong("positions")?.toInt() ?: 0,
                        requirements = doc.getString("requirements") ?: "",
                        type = doc.getString("type") ?: "",
                        tags = tagsList  // Add tags to JobListing
                    )
                }
                (binding.recyclerViewJobListings.adapter as JobListingAdapter).updateData(
                    jobListingsFull
                )
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    context,
                    "Error loading job postings: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
    }


    private fun setupSearchView() {
        val searchEditText = binding.searchEditText
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for this implementation
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for this implementation
            }

            override fun afterTextChanged(s: Editable?) {
                filterJobPostings(s.toString())
            }
        })
    }


    private fun filterJobPostings(query: String) {
        val filteredList = jobListingsFull.filter {
            it.title.contains(query, ignoreCase = true)
        }
        (binding.recyclerViewJobListings.adapter as JobListingAdapter).updateData(filteredList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
