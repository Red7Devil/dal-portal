package com.example.dalportal.ui.Admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.model.AdminStat
import com.example.dalportal.util.FirestoreHelper

/**
 * A fragment for displaying admin statistics.
 *
 * @author Shivam Lakhanpal
 */
class AdminFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AdminStatsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_admin_portal, container, false)

        recyclerView = view.findViewById(R.id.rvAdminStats)
        recyclerView.layoutManager = LinearLayoutManager(context)

        fetchAdminData()

        return view
    }

    /**
     * Fetches admin data from Firestore and updates the RecyclerView.
     */
    private fun fetchAdminData() {
        FirestoreHelper.fetchAdminData(onSuccess = { data ->
            val stats = data.map { AdminStat(it.key.replaceFirstChar { it.uppercase() }, it.value.toString()) }
            adapter = AdminStatsAdapter(stats)
            recyclerView.adapter = adapter
        }, onFailure = { exception ->
            Toast.makeText(context, "Error fetching admin data: ${exception.message}", Toast.LENGTH_LONG).show()
        })
    }
}
