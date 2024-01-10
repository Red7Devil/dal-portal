package com.example.dalportal.ui.assignments_review

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.dalportal.R
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.net.URLDecoder

class AssignmentFeedbackActivity : AppCompatActivity() {

    private val storage = FirebaseStorage.getInstance()
    private val storageRef = storage.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_assignment_feedback)
        FirebaseApp.initializeApp(this)

        // Retrieve assignment details from intent
        val assignmentName = intent.getStringExtra("assignmentName")
        val studentId = intent.getStringExtra("studentId")
        val fileUrl = intent.getStringExtra("fileUrl")

        // Set assignment details in the UI using findViewById
        val textAssignmentName = findViewById<TextView>(R.id.textAssignmentName)
        val textStudentId = findViewById<TextView>(R.id.textStudentId)

        textAssignmentName.text = "$assignmentName"
        textStudentId.text = "Student ID: $studentId"

        // Handle download button click
        val btnDownload = findViewById<Button>(R.id.btnDownload)
        btnDownload.setOnClickListener {
            // Extracting the part after the last '/' and before '?'
            val fileName = fileUrl?.substringAfterLast('/')?.substringBefore('?')
            val decodedFileName = URLDecoder.decode(fileName, "UTF-8")
            downloadFile(
                context = this,
                fileName = decodedFileName,
                fileExtension = ".pdf",   // Provide the desired file extension
                destinationDirectory = Environment.DIRECTORY_DOWNLOADS,
                url = "$fileUrl"
            )
        }


        // Handle submission button click
        val btnSubmit = findViewById<Button>(R.id.btnSubmit)
        btnSubmit.setOnClickListener {
            submitFeedback()
        }
    }

    private fun submitFeedback() {
        val score = findViewById<EditText>(R.id.editScore).text.toString()
        val feedback = findViewById<EditText>(R.id.editFeedback).text.toString()

        // Check if score and feedback are not empty
        if (score.isNotEmpty() && feedback.isNotEmpty()) {
            // Check if the score is between 0 and 100
            val numericScore = score.toIntOrNull()
            if (numericScore != null && numericScore in 0..100) {
                updateScoreAndFeedbackInFirestore(score, feedback)
            }
            else{
                // Show an error message if the score is not between 0 and 100
                showErrorAlert("Score must be between 0 and 100.")
            }
        } else {
            // Show an error message if either score or feedback is empty
            showErrorAlert("Score and feedback are required.")
        }
    }

    private fun updateScoreAndFeedbackInFirestore(score: String, feedback: String) {
        val db = FirebaseFirestore.getInstance()

        // Retrieve assignment details from intent
        val assignmentName = intent.getStringExtra("assignmentName")
        val studentId = intent.getStringExtra("studentId")

        // Reference to the specific assignment document
        val assignmentRef = db.collection("assignments")
            .whereEqualTo("studentId", studentId)
            .whereEqualTo("name", assignmentName)

        // Update score and feedback in Firestore
        assignmentRef.get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents[0]
                    // Update the document with new score and feedback
                    document.reference.update(
                        mapOf(
                            "score" to score,
                            "feedback" to feedback,
                            "graded" to true
                        )
                    )
                    showSuccessAlert()

                    // Immediately update the ViewModel to reflect the changes
                    val viewModel = ViewModelProvider(this).get(AssignmentsReviewViewModel::class.java)
                    val currentReviews = viewModel.reviews.value.orEmpty().toMutableList()
                    currentReviews.removeAll { it.studentId == studentId && it.name == assignmentName }
                    viewModel.updateReviews(currentReviews)


                } else {
                    showErrorAlert("File not found!")
                }
            }
            .addOnFailureListener { exception ->
                showErrorAlert(exception.message)
            }
    }

    private fun showAlert(title: String, message: String?) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("OK") { dialog, which -> dialog.dismiss() }
        builder.show()
    }

    private fun showSuccessAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Feedback Submitted Successfully!")
        builder.setPositiveButton("OK") { dialog, which ->
            // Dismiss the dialog
            dialog.dismiss()
            // Finish the current activity and return to the previous one
            finish()
        }
        builder.show()
    }

    private fun showErrorAlert(message: String?) {
        showAlert("Error", message)
    }

    private fun downloadFile(
        context: Context,
        fileName: String,
        fileExtension: String,
        destinationDirectory: String,
        url: String
    ) {
        if (url.isEmpty()) {
            showErrorAlert("File URL is empty or invalid.")
            return
        }

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)

        val request = DownloadManager.Request(uri).apply {
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            setDestinationInExternalPublicDir(destinationDirectory, "$fileName$fileExtension")
        }

        downloadManager.enqueue(request)
    }

}
