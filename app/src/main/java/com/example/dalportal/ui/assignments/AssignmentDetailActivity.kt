package com.example.dalportal.ui.assignments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.dalportal.R

class AssignmentDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_detail)

        // Retrieve assignment details from intent
        val assignmentName = intent.getStringExtra("assignmentName")
        val assignmentDeadline = intent.getStringExtra("assignmentDeadline")

        // Set assignment details as the activity title or fragment arguments
        supportActionBar?.title = assignmentName

        // Pass assignment details to the AssignmentDetailFragment
        val fragment = AssignmentDetailFragment.newInstance(assignmentName, assignmentDeadline)
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

    }
}