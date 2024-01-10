package com.example.dalportal.ui.DiscussionForm

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.dalportal.R
import com.example.dalportal.model.Post
import com.example.dalportal.util.FirestoreHelper

class PostAdapter(private var posts: MutableList<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]
        holder.postContent.text = post.content
        holder.likeCount.text = post.likes.toString()
        holder.commentCount.text = post.comments.toString()
        holder.replyTextView.text = post.reply ?: "No replies yet"

        holder.likeButton.setOnClickListener {
            post.likes++
            holder.likeCount.text = post.likes.toString()
            notifyItemChanged(position)
        }

        holder.commentButton.setOnClickListener {
            if (post.reply.isNullOrEmpty()) {
                showReplyDialog(holder.itemView.context, post, position)
            } else {
                Toast.makeText(holder.itemView.context, "Reply already exists", Toast.LENGTH_SHORT).show()
            }
        }

        // Display reply if it exists
        holder.replyTextView.visibility = if (post.reply.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    override fun getItemCount(): Int = posts.size

    fun updatePosts(newPosts: List<Post>) {
        posts.clear()
        posts.addAll(newPosts)
        notifyDataSetChanged()
    }

    private fun showReplyDialog(context: Context, post: Post, position: Int) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.reply_dialog, null)
        val replyEditText = dialogView.findViewById<EditText>(R.id.replyEditText)

        AlertDialog.Builder(context)
            .setTitle("Reply to Post")
            .setView(dialogView)
            .setPositiveButton("Reply") { dialog, _ ->
                val replyText = replyEditText.text.toString().trim()
                if (replyText.isNotEmpty()) {
                    post.reply = replyText
                    post.comments = 1 // Since only one reply is allowed, set to 1
                    notifyItemChanged(position)
                    FirestoreHelper.updatePostWithReply(post.id, replyText, onSuccess = {
                        Toast.makeText(context, "Reply added successfully", Toast.LENGTH_SHORT).show()
                    }, onFailure = { e ->
                        Toast.makeText(context, "Error adding reply: ${e.message}", Toast.LENGTH_SHORT).show()
                    })
                } else {
                    Toast.makeText(context, "Reply cannot be empty", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    class PostViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val postContent: TextView = view.findViewById(R.id.postContent)
        val likeButton: ImageView = view.findViewById(R.id.likeButton)
        val commentButton: ImageView = view.findViewById(R.id.commentButton)
        val likeCount: TextView = view.findViewById(R.id.likeCount)
        val commentCount: TextView = view.findViewById(R.id.commentCount)
        val replyTextView: TextView = view.findViewById(R.id.postReply) // Make sure this ID matches your layout
    }
}
