package com.example.dalportal.ui.chat.users

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.ui.chat.ChatActivity

class ChatUserAdapter() : RecyclerView.Adapter<ChatUserViewHolder>() {
    private var userList: List<User> = emptyList()
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatUserViewHolder {
        context = parent.context
        val view: View =
            LayoutInflater.from(context).inflate(R.layout.item_chat_user, parent, false)
        return ChatUserViewHolder(view)
    }

    override fun getItemCount(): Int = userList.size

    override fun onBindViewHolder(holder: ChatUserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ChatActivity::class.java)
            intent.putExtra("name", user.name)
            intent.putExtra("userId", user.userId)
            context.startActivity(intent)
        }
    }

    fun updateUsers(userList: List<User>) {
        this.userList = userList
        notifyDataSetChanged()
    }
}