package com.example.dalportal.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Post(
    // Exclude 'id' from Firestore as it's typically used for the document ID
    @Exclude var id: String = "",

    @PropertyName("user") var user: String = "",
    @PropertyName("content") var content: String = "",
    @PropertyName("timestamp") var timestamp: Long = System.currentTimeMillis(),
    @PropertyName("likes") var likes: Int = 0,
    @PropertyName("comments") var comments: Int = 0,
    var reply: String? = null
) {
    // Empty constructor for Firestore
    constructor() : this("", "", "", 0L, 0, 0)
}
