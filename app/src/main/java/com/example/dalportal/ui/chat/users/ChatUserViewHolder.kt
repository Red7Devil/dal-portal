package com.example.dalportal.ui.chat.users

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R

class ChatUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val userName: TextView = view.findViewById(R.id.chatUserName)

    fun bind(user: User) {
        userName.text = user.name
    }
}