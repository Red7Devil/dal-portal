package com.example.dalportal.ui.Admin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.model.AdminStat

/**
 * Adapter for displaying admin statistics in a RecyclerView.
 *
 * @property stats The data set of admin statistics.
 * @author Shivam Lakhanpal
 */
class AdminStatsAdapter(private val stats: List<AdminStat>) :
    RecyclerView.Adapter<AdminStatsAdapter.StatViewHolder>() {

    /**
     * Provides a reference to the views for each data item.
     *
     * @param view The root view of the item layout.
     */
    class StatViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val statNameTextView: TextView = view.findViewById(R.id.tvStatTitle)
        val statValueTextView: TextView = view.findViewById(R.id.tvStatValue)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.admin_stat_card, parent, false)
        return StatViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        val stat = stats[position]
        holder.statNameTextView.text = stat.name
        holder.statValueTextView.text = stat.value
    }

    override fun getItemCount() = stats.size
}
