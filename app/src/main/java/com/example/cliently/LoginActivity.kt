package com.example.cliently

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cliently.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: ClientlyDBHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = ClientlyDBHelper(this)

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (dbHelper.checkUser(username, password)) {
                Toast.makeText(this, "Welcome, $username!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, CustomerListActivity::class.java)
                intent.putExtra("LOGGED_IN_USER", username)
                startActivity(intent)
                finish()
            } else {
                binding.etUsername.error = null
                binding.etPassword.error = "Invalid username or password"
                Toast.makeText(this, "Invalid credentials. Try Sam / Sam 123", Toast.LENGTH_LONG).show()
            }
        }
    }
}
