package com.example.dalportal.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.ui.chat.users.ChatUserActivity
import com.example.dalportal.util.DashItems
import com.example.dalportal.util.UserData
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashboardFragment : Fragment() {

    private lateinit var chatButton: FloatingActionButton
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var dashRecyclerView: RecyclerView
    private lateinit var dashAdapter: DashboardAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(R.layout.fragment_dashboard, container, false)

        chatButton = view.findViewById(R.id.chatButton)
        chatButton.setOnClickListener {
            val intent =
                Intent(
                    context,
                    ChatUserActivity::class.java
                )
            startActivity(intent)
        }


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dashRecyclerView = view.findViewById(R.id.dashRecyclerView)
        dashRecyclerView.layoutManager = LinearLayoutManager(context)
        dashAdapter = DashboardAdapter { item ->
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.mobile_navigation, true)
                .build()
            val navId = navigateItem(item)
            findNavController().navigate(navId, null, navOptions)
        }
        dashRecyclerView.adapter = dashAdapter

        var dashItems: List<String> = DashItems[UserData.role]
        dashAdapter.updateDashItems(dashItems)
    }

    private fun navigateItem(item: String): Int {
        return when (item) {
            "assignments_submit" -> R.id.nav_assignment
            "events" -> R.id.nav_event
            "forum" -> R.id.nav_discussion
            "ta_portal" -> R.id.nav_ta_portal
            "update_availability" -> R.id.nav_availability_calendar
            "assignments_review" -> R.id.nav_assignment_review
            "content" -> R.id.nav_content
            "prof_portal" -> R.id.nav_professor_portal
            "review_availability" -> R.id.nav_availability_calendar_prof
            "admin_portal" -> R.id.nav_admin_portal
            "job_portal" -> R.id.nav_jobListing
            else -> 0
        }
    }
}