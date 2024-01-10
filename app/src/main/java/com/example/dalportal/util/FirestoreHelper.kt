package com.example.dalportal.util

import android.util.Log
import com.example.dalportal.model.Post
import com.example.dalportal.model.Users
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

object FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()

    fun addPost(post: Post, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .add(post)
            .addOnSuccessListener { documentReference ->
                Log.d("FirestoreHelper", "Post added successfully with ID: ${documentReference.id}")
                onSuccess(documentReference.id) // Pass the generated id back
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreHelper", "Error adding post", e)
                onFailure(e)
            }
    }

    fun fetchPosts(onSuccess: (List<Post>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("posts")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val posts = documents.mapNotNull { document ->
                    val post = document.toObject(Post::class.java)
                    post.id = document.id // Set the id of the Post object
                    post
                }
                onSuccess(posts)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreHelper", "Error fetching posts", e)
                onFailure(e)
            }
    }

    // This function updates both the reply text and the comments count
    fun updatePostWithReply(
        postId: String,
        reply: String,
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val postUpdate = mapOf(
            "reply" to reply,
            "comments" to 1 // Assuming each post can only have one reply, set comments to 1
        )
        db.collection("posts").document(postId)
            .update(postUpdate)
            .addOnSuccessListener {
                Log.d("FirestoreHelper", "Reply updated successfully for post ID: $postId")
                onSuccess()
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreHelper", "Error updating reply for post ID: $postId", e)
                onFailure(e)
            }
    }

    fun fetchAdminData(onSuccess: (Map<String, Any>) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("AdminData").document("MahXCiqtjy0xuCm62fVO")
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    onSuccess(documentSnapshot.data ?: emptyMap())
                } else {
                    onFailure(Exception("No Admin Data Found"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun addUser(user: Users, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("FirestoreHelper", "User added successfully with ID: ${documentReference.id}")
                onSuccess(documentReference.id) // Pass the generated id back
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreHelper", "Error adding user", e)
                onFailure(e)
            }
    }


    fun loginUser(
        email: String,
        password: String,
        onSuccess: (Boolean, String?, String?, String?, String?) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        db.collection("users")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    // Email not found
                    onSuccess(false, null, null, null, null)
                } else {
                    val user = documents.first().toObject(Users::class.java)
                    if (user.password == password) {
                        // Password matches, return user details
                        onSuccess(true, user.name, user.email, user.role, user.id)
                    } else {
                        // Password does not match
                        onSuccess(false, null, null, null, null)
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreHelper", "Error logging in user", e)
                onFailure(e)
            }
    }

    fun isEmailExist(email: String, onSuccess: (Boolean) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("users")
            .whereEqualTo("email", email)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                onSuccess(documents.size() > 0)
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreHelper", "Error checking email existence", e)
                onFailure(e)
            }
    }

    fun updateTaskStatus(status:String,taskId:String){
        val statusUpdate = mapOf("status" to status)
        db.collection("TA_tasks").document(taskId)
            .update(statusUpdate)
            .addOnSuccessListener {
                Log.d("FirestoreHelper", "Status updated successfully for task ID: $taskId")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreHelper", "Error updating status for task ID: $taskId", e)
            }
    }

}
