package com.example.dalportal.ui.assignments_review

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dalportal.model.AssignmentReview
import com.google.firebase.firestore.FirebaseFirestore

class AssignmentsReviewViewModel : ViewModel() {
    private val _reviews = MutableLiveData<List<AssignmentReview>>()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        // Fetch completed assignments from Firestore
        fetchCompletedAssignments()
    }

    fun updateReviews(updatedReviews: List<AssignmentReview>) {
        _reviews.postValue(updatedReviews)
    }

    private fun fetchCompletedAssignments() {
        db.collection("assignments")
            .whereEqualTo("completionStatus", "Completed")
            .whereEqualTo("graded", false)
            .addSnapshotListener { value, exception ->
                if (exception != null) {
                    // Handle error
                    return@addSnapshotListener
                }

                val completedAssignmentsList = mutableListOf<AssignmentReview>()

                for (document in value?.documents.orEmpty()) {
                    val assignmentReview = document.toObject(AssignmentReview::class.java)
                    assignmentReview?.let { completedAssignmentsList.add(it) }
                }

                _reviews.value = completedAssignmentsList
            }
    }


    val reviews: LiveData<List<AssignmentReview>> = _reviews
}
