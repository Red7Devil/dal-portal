package com.example.dalportal.ui.rating_system

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.dalportal.R
import com.google.firebase.firestore.FirebaseFirestore

class RatingViewFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating_view, container, false)

        val ratingsListView: ListView = view.findViewById(R.id.ratingsListView)

        // Fetch and display ratings
        fetchRatings(ratingsListView)

        return view
    }

    private fun fetchRatings(ratingsListView: ListView) {
        db.collection("ratings")
            .get()
            .addOnSuccessListener { result ->
                val ratingsList = mutableListOf<String>()

                for (document in result) {
                    val rating = document.data["rating"]
                    val comment = document.data["comment"]
                    val username = document.data["username"]

                    val ratingInfo = "$username ($rating): $comment"
                    ratingsList.add(ratingInfo)
                }

                // Display ratings in the ListView using ArrayAdapter
                val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, ratingsList)
                ratingsListView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                // Handle errors
            }
    }
}