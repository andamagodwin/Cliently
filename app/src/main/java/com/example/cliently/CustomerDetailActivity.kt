package com.example.cliently

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cliently.databinding.ActivityCustomerDetailBinding

/**
 * CustomerDetailActivity presents the profile and order history for a single customer.
 * This satisfies the project requirement for a SQL JOIN to pull together multi-table data.
 */
class CustomerDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerDetailBinding
    private lateinit var dbHelper: ClientlyDBHelper
    private var customerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve data passed from the previous activity via Intents
        customerId = intent.getIntExtra("CUSTOMER_ID", -1)
        val customerName = intent.getStringExtra("CUSTOMER_NAME") ?: "Customer"

        // Setup ActionBar with a "back" button enabled (setDisplayHomeAsUpEnabled)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = customerName
        supportActionBar?.subtitle = "Customer Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = ClientlyDBHelper(this)

        // Setup the list to display past orders
        binding.rvOrders.layoutManager = LinearLayoutManager(this)

        // Floating Action Button to navigate and add a new order
        binding.fabAddOrder.setOnClickListener {
            val intent = Intent(this, AddOrderActivity::class.java)
            // Send the customer info so the order is correctly linked
            intent.putExtra("CUSTOMER_ID", customerId)
            intent.putExtra("CUSTOMER_NAME", customerName)
            startActivity(intent)
        }

        // Load the static info (name, phone, email)
        loadCustomerDetails()
    }

    /**
     * Always refresh orders when we return from the "Add Order" screen.
     */
    override fun onResume() {
        super.onResume()
        loadOrderHistory()
    }

    /**
     * Populates the profile section with the customer's contact info.
     */
    private fun loadCustomerDetails() {
        val customer = dbHelper.getCustomerById(customerId) ?: return

        // Create an avatar string by taking the first letter of each name part
        // (Example: "Alice Nakato" -> "AN")
        val initials = customer.name.split(" ")
            .mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
            .take(2).joinToString("")
            
        binding.tvDetailAvatar.text = initials
        binding.tvDetailName.text = customer.name
        binding.tvDetailPhone.text = customer.phone.ifEmpty { "No phone on record" }
        binding.tvDetailEmail.text = customer.email.ifEmpty { "No email on record" }
    }

    /**
     * Loads and displays the specific customer's order history.
     * The dbHelper uses an INNER JOIN here to show Product names instead of just IDs.
     */
    private fun loadOrderHistory() {
        // Query the database for orders linked to this customerId
        val orders = dbHelper.getOrdersByCustomer(customerId)
        
        // Pass the results to our OrderAdapter
        val adapter = OrderAdapter(orders)
        binding.rvOrders.adapter = adapter

        // Update UI logic: show how many orders exist
        binding.tvOrderCount.text = "${orders.size} order${if (orders.size != 1) "s" else ""}"

        // Handle empty orders visibility
        if (orders.isEmpty()) {
            binding.layoutNoOrders.visibility = View.VISIBLE
            binding.rvOrders.visibility = View.GONE
        } else {
            binding.layoutNoOrders.visibility = View.GONE
            binding.rvOrders.visibility = View.VISIBLE
        }
    }

    /**
     * Standard back button behavior for the ActionBar back arrow.
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
