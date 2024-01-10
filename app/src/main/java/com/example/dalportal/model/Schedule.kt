package com.example.dalportal.model

data class Schedule(
    val user_id: String,
    val eventName: String,
    val date: String, // You can use a String representation of the date
    val time: String // You can use a String representation of the time
)