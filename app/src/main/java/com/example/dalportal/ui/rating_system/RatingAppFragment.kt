package com.example.dalportal.ui.rating_system

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.dalportal.util.UserData
import com.google.firebase.firestore.FirebaseFirestore
import com.example.dalportal.R

class RatingAppFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating_app, container, false)

        val ratingBar: RatingBar = view.findViewById(R.id.ratingBar)
        val commentEditText: EditText = view.findViewById(R.id.commentEditText)
        val submitBtn: Button = view.findViewById(R.id.submitBtn)

        submitBtn.setOnClickListener {
            val rating = ratingBar.rating
            val comment = commentEditText.text.toString()

            // Save data to Firebase
            saveDataToFirebase(rating, comment)
        }

        return view
    }

    private fun saveDataToFirebase(rating: Float, comment: String) {
        val data = hashMapOf(
            "rating" to rating,
            "comment" to comment,
            "username" to UserData.name,
            "email" to UserData.email
        )

        db.collection("ratings")
            .add(data)
            .addOnSuccessListener {

                Toast.makeText(requireContext(), "Rating submitted successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->

                Toast.makeText(requireContext(), "Error submitting rating", Toast.LENGTH_SHORT).show()
            }
    }
}
