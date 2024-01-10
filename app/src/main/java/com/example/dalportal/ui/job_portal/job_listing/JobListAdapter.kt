package com.example.dalportal.ui.job_portal.job_listing

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.model.JobListing
import com.google.firebase.firestore.FirebaseFirestore

class JobListingAdapter(private var jobListings: MutableList<JobListing>,
                        private val context: Context,
                        private val itemClickListener: (JobListing) -> Unit) :
    RecyclerView.Adapter<JobListingAdapter.ViewHolder>() {

    private val db = FirebaseFirestore.getInstance()

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewJobTitle: TextView = view.findViewById(R.id.textViewJobTitle)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
        val textViewJobTags:TextView = view.findViewById(R.id.textViewJobTags)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_job_listing, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val jobListing = jobListings[position]
        holder.textViewJobTitle.text = jobListing.title
        holder.textViewJobTags.text = jobListing.tags.joinToString(", ")
        holder.deleteButton.setOnClickListener {
            deleteJob(  holder.itemView.context, jobListing, position)
        }
        holder.itemView.setOnClickListener {
            itemClickListener(jobListing)
        }
    }

    override fun getItemCount() = jobListings.size

    fun updateData(newJobListings: List<JobListing>) {
        jobListings = newJobListings.toMutableList()
        notifyDataSetChanged()
    }

    private fun deleteJob(context: Context, jobListing: JobListing, position: Int) {
        deleteJobFromDatabase(context, jobListing.id)
        jobListings.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun deleteJobFromDatabase(context: Context, jobId: String) {
        db.collection("jobPostings").document(jobId)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(context, "Job deleted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Error deleting job: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
