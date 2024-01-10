package com.example.dalportal.model

import com.google.firebase.Timestamp

data class TA_task(
    val taskId:String,
    val description: String,
    val deadline: Timestamp,
    val priority: String,
    val assignedTo: String,
    val assignedBy: String,
    val status: String
)
