package com.example.dalportal.ui.ta_portal.firebase

import com.google.firebase.Timestamp
import com.google.firebase.firestore.PropertyName
import java.util.Date

data class TaTasksModel(
    @PropertyName("id")
    var id: String = "",

    @PropertyName("assignedTo")
    val assignedTo: String = "",

    @PropertyName("assignedBy")
    val assignedBy: String = "",

    @PropertyName("description")
    val description: String = "",

    @PropertyName("deadline")
    val deadline: Date? = null,

    @PropertyName("priority")
    val priority: String = "",

    @PropertyName("status")
    val status: String = "",

    @PropertyName("timestamp")
    val timestamp: Date = Date(),

    @PropertyName("profName")
    val profName: String = ""
)

