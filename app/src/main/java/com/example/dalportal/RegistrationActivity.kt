package com.example.dalportal

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.dalportal.model.Users
import com.example.dalportal.ui.chat.users.User
import com.example.dalportal.util.FirestoreHelper
import com.example.dalportal.util.UserData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val roleSpinner = findViewById<Spinner>(R.id.roleSpinner)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val alreadyRegisteredButton = findViewById<Button>(R.id.alreadyRegisteredButton)

        alreadyRegisteredButton.setOnClickListener {
            redirectToLogin()
        }

        // Setting up the spinner
        ArrayAdapter.createFromResource(
            this, R.array.role_options, android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            roleSpinner.adapter = adapter
        }

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val role = roleSpinner.selectedItem.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || role.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            } else if (!isEmailValid(email)) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
            } else {
                FirestoreHelper.isEmailExist(email, onSuccess = { exists ->
                    if (exists) {
                        Toast.makeText(this, "Email already exists", Toast.LENGTH_SHORT).show()
                    } else {
                        // Generate a unique ID based on the role
                        val userId = generateUserId(role)
                        val newUser = Users(
                            id = userId,
                            name = name,
                            email = email,
                            password = password,
                            role = role
                        )
                        FirestoreHelper.addUser(newUser, onSuccess = {
                            Toast.makeText(
                                this, "Registered successfully", Toast.LENGTH_SHORT
                            ).show()
                            UserData.id = userId
                            UserData.name = name
                            UserData.email = email
                            UserData.role = role
                            redirectToHomePage()
                            addUserToRealtimeDb(newUser)
                        }, onFailure = { exception ->
                            Toast.makeText(
                                this,
                                "Registration failed: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    }
                }, onFailure = { exception ->
                    Toast.makeText(
                        this, "Error checking email: ${exception.message}", Toast.LENGTH_SHORT
                    ).show()
                })
            }
        }
    }

    private fun addUserToRealtimeDb(newUser: Users) {
        dbRef = FirebaseDatabase.getInstance().reference
        dbRef.child("users").child(newUser.id)
            .setValue(User(newUser.name, newUser.email, newUser.id, newUser.role))
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun redirectToHomePage() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun isEmailValid(email: String): Boolean {
        val emailRegex = "^[A-Za-z](.*)([@]{1})(.{1,})(\\.)(.{1,})"
        return email.matches(emailRegex.toRegex())
    }

    private fun generateUserId(role: String): String {
        val prefix = when (role) {
            "Professor" -> "P0"
            "Student" -> "S0"
            "TA" -> "T0"
            else -> "U0" // Default prefix for unknown roles
        }
        val randomPart = (10000000..99999999).random().toString()
        return "$prefix$randomPart"
    }
}
