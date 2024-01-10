package com.example.dalportal.ui.chat.messages

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import java.text.SimpleDateFormat

class ChatSentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val sentMessage: TextView = view.findViewById(R.id.chatSentMessage)
    private val sentTimeView: TextView = view.findViewById(R.id.sentChatTime)

    fun bind(message: Message) {
        val sentTime = message.timestamp
        var formatter = SimpleDateFormat("dd-MM-yyyy HH:MM")
        val dateString: String = formatter.format(sentTime)

        sentMessage.text = message.message
        sentTimeView.text = dateString

    }
}