package com.example.dalportal.ui.chat.messages

import java.util.Date

data class Message(
    var message: String? = "",
    var senderId: String? = "",
    val timestamp: Date? = Date()
)
