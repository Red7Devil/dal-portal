package com.example.dalportal.ui.chat

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.ui.chat.messages.ChatMessageAdapter
import com.example.dalportal.ui.chat.messages.Message
import com.example.dalportal.ui.chat.users.ChatUserActivity
import com.example.dalportal.util.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Date

class ChatActivity : AppCompatActivity() {
    private lateinit var messageRecyclerView: RecyclerView
    private lateinit var chatEditText: EditText
    private lateinit var sendButton: ImageView
    private lateinit var messageAdapter: ChatMessageAdapter
    private lateinit var messageList: MutableList<Message>
    private lateinit var dbRef: DatabaseReference

    private var receiverRoom: String? = null
    private var senderRoom: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val recName = intent.getStringExtra("name")
        val recUserId = intent.getStringExtra("userId")

        senderRoom = recUserId + UserData.id
        receiverRoom = UserData.id + recUserId

        supportActionBar?.title = recName
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }

        dbRef = FirebaseDatabase.getInstance().reference

        messageRecyclerView = findViewById(R.id.messageRecyclerView)
        chatEditText = findViewById(R.id.chatEditText)
        sendButton = findViewById(R.id.sendButton)

        messageList = mutableListOf()
        messageAdapter = ChatMessageAdapter(recName)
        messageRecyclerView.layoutManager = LinearLayoutManager(this)
        messageRecyclerView.adapter = messageAdapter

        dbRef.child("chats").child(senderRoom!!).child("messages")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()

                    for (snap in snapshot.children) {
                        val message = snap.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.updateMessages(messageList)
                    scrollToBottom()
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        sendButton.setOnClickListener {
            val message = chatEditText.text.toString()
            val timestamp = Date()
            val messageObj = Message(message, UserData.id, timestamp)
            if (message.isNotEmpty()) {
                dbRef.child("chats").child(senderRoom!!).child("messages").push()
                    .setValue(messageObj)
                    .addOnSuccessListener {
                        dbRef.child("chats").child(receiverRoom!!).child("messages").push()
                            .setValue(messageObj)
                    }
                chatEditText.setText("")
            }

        }
    }

    private fun scrollToBottom() {
        if (messageAdapter.itemCount > 0) {
            messageRecyclerView.scrollToPosition(messageAdapter.itemCount - 1)
        }
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, ChatUserActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val intent = Intent(this, ChatUserActivity::class.java)
        startActivity(intent)
    }
}