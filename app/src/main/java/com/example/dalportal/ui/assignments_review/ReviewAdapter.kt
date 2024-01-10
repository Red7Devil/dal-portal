package com.example.dalportal.ui.assignments_review

import android.content.Intent
import android.text.style.UnderlineSpan
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.model.AssignmentReview

class ReviewAdapter(private var reviews: List<AssignmentReview>) :
    RecyclerView.Adapter<ReviewAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val assignmentName: TextView = view.findViewById(R.id.textAssignmentName)
        val studentId: TextView = view.findViewById(R.id.textStudentId)
        val fileUrl: TextView = view.findViewById(R.id.textFileUrl)
        val deadline: TextView = view.findViewById(R.id.reviewDeadline)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val review = reviews[position]
        holder.assignmentName.text = review.name
        holder.studentId.text = review.studentId
        holder.deadline.text = "Due on ${review.deadline}"
        val customUrl = "ASSIGNMENT URL"
        val spanUrl = SpannableString(customUrl)
        spanUrl.setSpan(UnderlineSpan(), 0, spanUrl.length, 0)
        holder.fileUrl.text = spanUrl

        // Handle item click to open AssignmentFeedbackActivity
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, AssignmentFeedbackActivity::class.java).apply {
                putExtra("assignmentName", review.name)
                putExtra("studentId", review.studentId)
                putExtra("fileUrl", review.fileUrl)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return reviews.size
    }

    // Function to update the data in the adapter
    fun updateReviews(newReviews: List<AssignmentReview>) {
        reviews = newReviews
        notifyDataSetChanged()
    }
}