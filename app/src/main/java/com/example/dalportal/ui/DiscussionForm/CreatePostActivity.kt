package com.example.dalportal.ui.DiscussionForm

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dalportal.R
import com.example.dalportal.model.Post
import com.example.dalportal.util.FirestoreHelper

class CreatePostActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.create_post)

        val submitButton = findViewById<Button>(R.id.submitPostButton)
        val postInput = findViewById<EditText>(R.id.postInput)

        submitButton.setOnClickListener {
            val postContent = postInput.text.toString()
            if (postContent.length > 500) {
                Toast.makeText(this, "Post content exceeds 500 characters", Toast.LENGTH_LONG).show()
            } else {
                // Call a function to handle the submission of the post
                submitPost(postContent)
            }
        }
    }

    private fun submitPost(content: String) {
        val newPost = Post(
            user = "Username", // Replace with actual user identifier
            content = content
        )
    if(!newPost.content.trim().isNullOrEmpty()) {
        FirestoreHelper.addPost(newPost, onSuccess = {
            // Handle success
            Toast.makeText(this, "Post added successfully", Toast.LENGTH_SHORT).show()
            finish() // Close the activity
        }, onFailure = { e ->
            // Handle failure
            Toast.makeText(this, "Error adding post: ${e.message}", Toast.LENGTH_SHORT).show()
        })
    }
        else{
        Toast.makeText(this, "You cannot submit an empty discussion post.", Toast.LENGTH_SHORT).show()
    }
    }

}
