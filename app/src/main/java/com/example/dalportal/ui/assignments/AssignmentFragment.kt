package com.example.dalportal.ui.assignments

import AssignmentAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dalportal.databinding.FragmentAssignmentBinding

class AssignmentFragment : Fragment() {

    private var _binding: FragmentAssignmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var assignmentViewModel: AssignmentViewModel
    private lateinit var adapter: AssignmentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssignmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        assignmentViewModel =
            ViewModelProvider(this).get(AssignmentViewModel::class.java)

        // Observe changes in assignments LiveData
        assignmentViewModel.assignments.observe(viewLifecycleOwner, { assignments ->
            // Update RecyclerView adapter when data changes
            adapter.updateAssignments(assignments)
        })

        // Initialize RecyclerView
        adapter = AssignmentAdapter(emptyList()) // Initial adapter with empty data
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
