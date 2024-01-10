package com.example.dalportal.ui.assignments_review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dalportal.R
import com.example.dalportal.databinding.FragmentAssignmentsReviewBinding

class AssignmentsReviewFragment : Fragment() {

    private var _binding: FragmentAssignmentsReviewBinding? = null
    private val binding get() = _binding!!
    private lateinit var assignmentsReviewViewModel: AssignmentsReviewViewModel
    private lateinit var adapter: ReviewAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentsReviewBinding.inflate(inflater, container, false)
        val root: View = binding.root

        assignmentsReviewViewModel =
            ViewModelProvider(this).get(AssignmentsReviewViewModel::class.java)

        // Observe changes in reviews LiveData
        assignmentsReviewViewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            // Update RecyclerView adapter when data changes
            adapter.updateReviews(reviews)
        }

        binding.addAssignmentBtn.setOnClickListener {
            findNavController().navigate(R.id.action_assignmentReview_to_addAssignment)
        }

        // Initialize RecyclerView
        adapter = ReviewAdapter(emptyList()) // Initial adapter with empty data
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}