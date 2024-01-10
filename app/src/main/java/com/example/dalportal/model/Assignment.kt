package com.example.dalportal.model

data class Assignment(
    var name: String = "",
    var deadline: String = "",
    var completionStatus: String = "",
    var score: String = "",
    var fileUrl: String = "",
    var studentId: String = "",
    var feedback: String = "",
    var description: String = "",
    var graded: Boolean = false
)
