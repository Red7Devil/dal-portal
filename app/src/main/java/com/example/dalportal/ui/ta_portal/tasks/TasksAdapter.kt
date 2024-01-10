package com.example.dalportal.ui.ta_portal.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.ui.ta_portal.firebase.TaTasksModel

class TasksAdapter : RecyclerView.Adapter<TaskViewHolder>() {
    private var tasks: List<TaTasksModel> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_ta_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun getItemCount(): Int = tasks.size

    fun updateTasks(tasks: List<TaTasksModel>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }
}