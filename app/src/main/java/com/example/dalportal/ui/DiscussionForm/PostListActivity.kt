package com.example.dalportal.ui.DiscussionForm

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.model.Post
import com.example.dalportal.util.FirestoreHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostListActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.post_list)

        recyclerView = findViewById(R.id.postsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = PostAdapter(mutableListOf()) // Initialize with an empty mutable list
        recyclerView.adapter = adapter

        val fab: FloatingActionButton = findViewById(R.id.fabCreatePost)
        fab.setOnClickListener {
            val intent = Intent(this, CreatePostActivity::class.java)
            startActivity(intent)
        }

        fetchPosts()
    }

    private fun fetchPosts() {
        FirestoreHelper.fetchPosts(onSuccess = { posts ->
            Log.d("PostListActivity", "Number of posts fetched: ${posts.size}")
            adapter.updatePosts(posts as MutableList<Post>)
        }, onFailure = { exception ->
            Toast.makeText(this, "Error fetching posts: ${exception.message}", Toast.LENGTH_LONG).show()
        })
    }
    override fun onResume() {
        super.onResume()
        fetchPosts()
    }

}
