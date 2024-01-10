package com.example.dalportal.ui.ta_portal.tasks

import android.app.AlertDialog
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.ui.ta_portal.firebase.TaTasksModel
import com.example.dalportal.util.FirestoreHelper
import com.example.dalportal.util.TaskStatus
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val deadlineTextView: TextView = view.findViewById(R.id.deadline)
    private val deadlineTimeTextView: TextView = view.findViewById(R.id.deadlineTime)
    private val detailsTextView: TextView = view.findViewById(R.id.taskDetails)
    private val professorTextView: TextView = view.findViewById(R.id.taskProfessor)
    private val statusTextView: TextView = view.findViewById(R.id.taskStatus)
    private val spinnerItems: Array<String> =
        view.context.resources.getStringArray(R.array.ta_edit_status_spinner_items)
    private val context = view.context
    private val priorityIcon: TextView = view.findViewById(R.id.priorityIcon)

    fun bind(task: TaTasksModel) {
        val deadline: Date? = task.deadline
        var formatter = SimpleDateFormat("dd-MM-yyyy HH:MM")
        val dateString: String = formatter.format(deadline)
        val daysLeft: Long? = deadline?.let { daysUntilDeadline(it) }
        val priority = task.priority

        setPriorityIcon(priority)

        detailsTextView.text = task.description
        deadlineTextView.text = dateString
        professorTextView.text = task.profName

        if (daysLeft != null) {
            setDaysLeft(daysLeft)
        }

        val statusString = TaskStatus.valueOf(task.status).statusString
        setStatus(statusString, task.id)

        statusTextView.setOnClickListener {
            AlertDialog.Builder(context)
                .setItems(spinnerItems) { dialog, which ->
                    when (val status = spinnerItems[which]) {
                        TaskStatus.valueOf(task.status).statusString -> {}
                        else -> setStatus(status, task.id)
                    }
                }
                .show()
        }
    }

    private fun setPriorityIcon(priority: String) {
        val priorities = context?.resources?.getStringArray(R.array.priority_levels)

        priorityIcon.setOnClickListener {
            priorityIcon.performLongClick()
        }
        when (priority.lowercase(Locale.ROOT)) {
            priorities?.get(0)?.lowercase(Locale.ROOT) -> {
                priorityIcon.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_high_priority,
                    0,
                    0,
                    0
                )
                priorityIcon.tooltipText = "${priorities?.get(0)} Priority"
                priorityIcon.setTextColor(
                    itemView.context.getColor(
                        R.color.red
                    )
                )
            }

            priorities?.get(1)?.lowercase(Locale.ROOT) -> {
                priorityIcon.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_med_priority,
                    0,
                    0,
                    0
                )
                priorityIcon.tooltipText = "${priorities?.get(1)} Priority"
                priorityIcon.setTextColor(
                    itemView.context.getColor(
                        R.color.yellow_electric
                    )
                )
            }

            priorities?.get(2)?.lowercase(Locale.ROOT) -> {
                priorityIcon.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_low_priority,
                    0,
                    0,
                    0
                )
                priorityIcon.tooltipText = "${priorities?.get(2)} Priority"
                priorityIcon.setTextColor(
                    itemView.context.getColor(
                        R.color.green
                    )
                )
            }

            else -> {}
        }

    }

    private fun setStatus(status: String, taskId: String) {
        statusTextView.text = status

        when (status) {
            TaskStatus.PENDING.statusString -> {
                statusTextView.setTextColor(
                    itemView.context.getColor(
                        R.color.orange
                    )
                )
                statusTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_pending_orange,
                    0,
                    R.drawable.ic_edit_property,
                    0,

                    )
                FirestoreHelper.updateTaskStatus(
                    TaskStatus.PENDING.toString(),
                    taskId
                )
            }

            TaskStatus.COMPLETED.statusString -> {
                statusTextView.setTextColor(
                    itemView.context.getColor(
                        R.color.green
                    )
                )
                statusTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_success_green,
                    0,
                    R.drawable.ic_edit_property,
                    0,
                )

                FirestoreHelper.updateTaskStatus(
                    TaskStatus.COMPLETED.toString(),
                    taskId
                )
            }

            else -> {
                statusTextView.setTextColor(itemView.context.getColor(R.color.cobalt_blue))
                statusTextView.setCompoundDrawablesWithIntrinsicBounds(
                    R.drawable.ic_in_progress_blue,
                    0,
                    R.drawable.ic_edit_property,
                    0,
                )
                FirestoreHelper.updateTaskStatus(
                    TaskStatus.IN_PROGRESS.toString(),
                    taskId
                )
            }
        }
    }

    fun setDaysLeft(daysLeft: Long) {
        if (daysLeft != null) {
            if (daysLeft > 0) {
                deadlineTimeTextView.text = "$daysLeft days left"
                deadlineTimeTextView.visibility = View.VISIBLE
                if (daysLeft < 3) {
                    deadlineTimeTextView.setTextColor(
                        itemView.context.getColor(
                            R.color.orange
                        )
                    )
                } else {
                    deadlineTimeTextView.setTextColor(
                        itemView.context.getColor(
                            R.color.cobalt_blue
                        )
                    )
                }
            } else if (daysLeft < 0) {
                deadlineTimeTextView.text = "${-daysLeft} days late"
                deadlineTimeTextView.visibility = View.VISIBLE
                deadlineTimeTextView.setTextColor(
                    itemView.context.getColor(
                        R.color.red
                    )
                )
            } else {
                deadlineTimeTextView.visibility = View.GONE
            }
        }
    }

    fun daysUntilDeadline(deadline: Date): Long {
        val currentDate = Calendar.getInstance().time
        val diffInMillies = deadline.time - currentDate.time
        return TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }
}