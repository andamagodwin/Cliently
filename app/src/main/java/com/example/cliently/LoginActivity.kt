package com.example.cliently

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cliently.databinding.ActivityLoginBinding

/**
 * LoginActivity is the entry point of the application.
 * It handles authenticating users against the local SQLite database.
 */
class LoginActivity : AppCompatActivity() {

    // View Binding allows us to interact with our XML elements without using findViewById()
    private lateinit var binding: ActivityLoginBinding
    
    // Instance of our Database Helper to perform queries
    private lateinit var dbHelper: ClientlyDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Setup View Binding and the UI layout
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize the database helper
        dbHelper = ClientlyDBHelper(this)

        /**
         * The click listener for the Sign In button.
         * It extracts the username and password, validates them, and checks the database.
         */
        binding.btnLogin.setOnClickListener {
            // Get text from Input fields and trim any accidental whitespace
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            // Step 1: Basic validation - ensure no fields are left empty
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Step 2: Database lookup - call checkUser from dbHelper
            if (dbHelper.checkUser(username, password)) {
                // SUCCESS: Notify player and transition to the next activity
                Toast.makeText(this, "Welcome, $username!", Toast.LENGTH_SHORT).show()
                
                // Intents are used for navigating between Activities in Android
                val intent = Intent(this, CustomerListActivity::class.java)
                intent.putExtra("LOGGED_IN_USER", username) // Pass data forward
                startActivity(intent)
                
                // finish() closes the login screen so the user can't press "Back" to return to it
                finish()
                
            } else {
                // FAILURE: Show error hints and a toast with default credentials
                binding.etUsername.error = null
                binding.etPassword.error = "Invalid username or password"
                Toast.makeText(this, "Invalid credentials. Try Sam / Sam 123", Toast.LENGTH_LONG).show()
            }
        }
    }
}
