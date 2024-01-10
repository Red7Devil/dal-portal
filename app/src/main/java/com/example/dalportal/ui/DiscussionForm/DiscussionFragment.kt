package com.example.dalportal.ui.DiscussionForm

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.model.Post
import com.example.dalportal.util.FirestoreHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DiscussionFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.post_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.postsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = PostAdapter(mutableListOf())
        recyclerView.adapter = adapter

        val fab: FloatingActionButton = view.findViewById(R.id.fabCreatePost)
        fab.setOnClickListener {
            val intent = Intent(context, CreatePostActivity::class.java)
            startActivity(intent)
        }

        fetchPosts()
    }

    private fun fetchPosts() {
        FirestoreHelper.fetchPosts(onSuccess = { posts ->
            adapter.updatePosts(posts as MutableList<Post>)
        }, onFailure = { exception ->
            Toast.makeText(context, "Error fetching posts: ${exception.message}", Toast.LENGTH_LONG).show()
        })
    }
}
