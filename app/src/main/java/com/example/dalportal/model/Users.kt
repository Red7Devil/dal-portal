package com.example.dalportal.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.PropertyName

data class Users(
        @Exclude var id: String = "",

        @PropertyName("name") var name: String = "",
        @PropertyName("email") var email: String = "",
        @PropertyName("password") var password: String = "",
        @PropertyName("role") var role: String = ""
) {
    // Empty constructor for Firestore
    constructor() : this("", "", "", "", "")
}
