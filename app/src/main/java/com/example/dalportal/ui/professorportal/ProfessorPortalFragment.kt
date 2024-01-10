package com.example.dalportal.ui.professorportal

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.dalportal.R
import com.example.dalportal.databinding.FragmentProfessorPortalBinding
import com.example.dalportal.ui.chat.users.ChatUserActivity

class ProfessorPortalFragment : Fragment() {

    private var _binding: FragmentProfessorPortalBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val porfessorPortalViewModel =
            ViewModelProvider(this).get(ProfessorPortalViewModel::class.java)

        _binding = FragmentProfessorPortalBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textGalleryProfessor
        val assignTaskbutton: Button = binding.assignAtaskbutton
        val progressMonitoringbutton: Button = binding.progressMonitoringButton
        val overviewDashboardButton: Button = binding.assesPerformanceButton
        val coursescheduleButton: Button = binding.courseScheduleButton

        porfessorPortalViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        assignTaskbutton.setOnClickListener {
            // Navigate to AssignTaskFragment
            findNavController().navigate(R.id.action_professor_to_assign_task)
        }
        progressMonitoringbutton.setOnClickListener {
            // Navigate to progress monitor task here
            findNavController().navigate(R.id.action_professor_to_task_dashboard)
        }
        overviewDashboardButton.setOnClickListener {
            // Navigate to overview dashboard
            findNavController().navigate(R.id.action_professor_to_overview_dashboard)
        }

        coursescheduleButton.setOnClickListener {
            val intent = Intent(requireContext(), CalendarActivity::class.java)
            startActivity(intent)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}