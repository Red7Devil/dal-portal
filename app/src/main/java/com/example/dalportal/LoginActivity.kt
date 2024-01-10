// LoginActivity.kt

package com.example.dalportal

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.dalportal.util.FirestoreHelper
import com.example.dalportal.util.UserData

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        UserData.clear()

        val emailEditText = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val passwordEditText = findViewById<EditText>(R.id.editTextTextPassword)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val createAccountButton = findViewById<Button>(R.id.createAccountButton)

        createAccountButton.setOnClickListener {
            redirectToRegistration()
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                FirestoreHelper.loginUser(email, password,
                    onSuccess = { success, name, email, role, userId ->
                        if (success) {
                            Toast.makeText(this, "Login successful for $name", Toast.LENGTH_SHORT)
                                .show()
                            UserData.name = name
                            UserData.email = email
                            UserData.role = role
                            UserData.id = userId
                            val email = emailEditText.text.toString().trim()
                            val password = passwordEditText.text.toString().trim()

                            redirectToHomePage() // Redirect to home page only
                        } else {
                            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    onFailure = { exception ->
                        Toast.makeText(
                            this,
                            "Login failed: ${exception.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        }
    }

    private fun redirectToHomePage() {
        val intent =
            Intent(this, MainActivity::class.java) // Make sure MainActivity is your home page
        startActivity(intent)
        finish() // Close the login activity
    }

    private fun redirectToRegistration() {
        val intent = Intent(this, RegistrationActivity::class.java)
        startActivity(intent)
    }
}
