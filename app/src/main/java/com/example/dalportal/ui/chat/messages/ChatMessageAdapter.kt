package com.example.dalportal.ui.chat.messages

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.dalportal.R
import com.example.dalportal.ui.chat.users.ChatUserViewHolder
import com.example.dalportal.util.UserData

class ChatMessageAdapter(val recName: String? = "") : RecyclerView.Adapter<ViewHolder>() {
    private var messageList: List<Message> = emptyList()
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        if (viewType == ITEM_RECEIVE) {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_recd, parent, false)
            return ChatRecdViewHolder(view)
        } else {
            val view: View =
                LayoutInflater.from(parent.context).inflate(R.layout.item_chat_sent, parent, false)
            return ChatSentViewHolder(view)
        }
    }

    override fun getItemCount(): Int = messageList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (holder.javaClass == ChatSentViewHolder::class.java) {
            val viewHolder = holder as ChatSentViewHolder
            holder.bind(messageList[position])
        } else {
            val viewHolder = holder as ChatRecdViewHolder
            holder.bind(messageList[position],recName)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = messageList[position]
        return if (UserData.id == message.senderId) {
            ITEM_SENT
        } else {
            ITEM_RECEIVE
        }
    }

    fun updateMessages(messageList: List<Message>) {
        this.messageList = messageList
        notifyDataSetChanged()
    }
}