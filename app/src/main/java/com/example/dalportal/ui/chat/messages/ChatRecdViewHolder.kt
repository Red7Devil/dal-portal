package com.example.dalportal.ui.chat.messages

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import java.text.SimpleDateFormat

class ChatRecdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val recdMessage: TextView = view.findViewById(R.id.chatRecdMessage)
    private val recdTimeView: TextView = view.findViewById(R.id.recdChatTime)
    private val recdName: TextView = view.findViewById(R.id.chatRecName)

    fun bind(message: Message, recName: String? = "") {
        val recdTime = message.timestamp
        var formatter = SimpleDateFormat("dd-MM-yyyy HH:MM")
        val dateString: String = formatter.format(recdTime)

        recdMessage.text = message.message
        recdTimeView.text = dateString
        recdName.text = recName
    }
}