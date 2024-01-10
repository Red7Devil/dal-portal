package com.example.dalportal.ui.dashboard

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R

class DashboardAdapter(private val onItemClicked: (String) -> Unit) :
    RecyclerView.Adapter<DashboardViewHolder>() {
    private var dashItems: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_dashboard, parent, false)
        return DashboardViewHolder(view)
    }

    override fun getItemCount(): Int = dashItems.size

    override fun onBindViewHolder(holder: DashboardViewHolder, position: Int) {
        holder.bind(dashItems[position], onItemClicked)
    }

    fun updateDashItems(dashItems: List<String>) {
        this.dashItems = dashItems
        notifyDataSetChanged()
    }

}