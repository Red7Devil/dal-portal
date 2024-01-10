package com.example.dalportal.ui.assignments_review.add_assignment

import android.provider.ContactsContract.CommonDataKinds.Email

data class UserDetails(
    val id: String? = "",
    val name: String? = "",
    val role: String? = "",
    var email: String? = ""
)
