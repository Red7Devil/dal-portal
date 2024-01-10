package com.example.dalportal.ui.chat.users

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.MainActivity
import com.example.dalportal.R
import com.example.dalportal.util.UserData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatUserActivity : AppCompatActivity() {
    private lateinit var userRecylerView: RecyclerView
    private val adapter: ChatUserAdapter = ChatUserAdapter()
    private lateinit var dbRef: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_user_activty)

        val userList: MutableList<User> = mutableListOf()

        userRecylerView = findViewById(R.id.chatUserRecyclerView)
        userRecylerView.layoutManager = LinearLayoutManager(this)
        userRecylerView.adapter = adapter

        supportActionBar?.title = "Chat"
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
        }


        dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (snap in snapshot.children) {
                    val user = snap.getValue(User::class.java)
                    if (user != null && user.userId != UserData.id) {
                        userList.add(user)
                    }
                }

                adapter.updateUsers(userList)
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    @Override
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}