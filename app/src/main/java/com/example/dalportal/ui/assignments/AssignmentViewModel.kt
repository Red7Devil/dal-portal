package com.example.dalportal.ui.assignments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dalportal.model.Assignment
import com.example.dalportal.util.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AssignmentViewModel : ViewModel() {
    private val _assignments = MutableLiveData<List<Assignment>>()
    private val db: FirebaseFirestore = Firebase.firestore
    private var studentId: String? = UserData.id

    init {
        // Fetch assignments from Firestore
        fetchAssignments()
    }

    private fun fetchAssignments() {
        db.collection("assignments")
            .whereEqualTo("studentId", studentId)
            .addSnapshotListener { querySnapshot, exception ->
                if (exception != null) {
                    handleQueryFailure(exception)
                    return@addSnapshotListener
                }

                if (querySnapshot != null) {
                    handleQuerySuccess(querySnapshot)
                }
            }
    }


    private fun handleQuerySuccess(querySnapshot: QuerySnapshot) {
        val assignmentsList = mutableListOf<Assignment>()

        for (document in querySnapshot.documents) {
            val assignment = document.toObject(Assignment::class.java)
            assignment?.let { assignmentsList.add(it) }
        }

        _assignments.value = assignmentsList
    }

    private fun handleQueryFailure(exception: Exception) {
        // Handle error here
    }

    val assignments: LiveData<List<Assignment>> = _assignments
}
