package com.example.cliently

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cliently.databinding.ActivityAddOrderBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * AddOrderActivity provides a form to create a new transaction.
 * It uses a Spinner (dropdown) to select products from the database.
 */
class AddOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddOrderBinding
    private lateinit var dbHelper: ClientlyDBHelper
    private var customerId: Int = -1
    private var products: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Identify the customer this order belongs to
        customerId = intent.getIntExtra("CUSTOMER_ID", -1)
        val customerName = intent.getStringExtra("CUSTOMER_NAME") ?: "Customer"

        // Setup the UI toolbar with a back arrow
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "New Order"
        supportActionBar?.subtitle = "For $customerName"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = ClientlyDBHelper(this)

        // Pre-fill "Today's Date" automatically to make the user's life easier
        // uses the "yyyy-MM-dd" format which is standard for SQLite
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        binding.etOrderDate.setText(today)

        // Populate the product dropdown list
        loadProducts()

        // Handle the "Place Order" button click
        binding.btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    /**
     * Fetches all products from the database and puts them into the Spinner.
     */
    private fun loadProducts() {
        // Step 1: Query the product catalog
        products = dbHelper.getAllProducts()
        
        // Step 2: Format the labels for the dropdown list (Name + Price)
        val productNames = products.map { "${it.name} — ${NumberFormat.getInstance().format(it.price)} UGX" }
        
        // Step 3: Create an ArrayAdapter (Bridge between our list and the Spinner UI component)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        
        // Step 4: Attach the adapter to the Spinner
        binding.spinnerProduct.adapter = adapter
    }

    /**
     * Collects form data and saves the new order record to the database.
     */
    private fun placeOrder() {
        if (products.isEmpty()) {
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show()
            return
        }

        // Step 1: Get selected product from the Spinner
        val selectedIndex = binding.spinnerProduct.selectedItemPosition
        val selectedProduct = products[selectedIndex]
        
        // Step 2: Get and validate the date string
        val date = binding.etOrderDate.text.toString().trim()
        if (date.isEmpty()) {
            binding.etOrderDate.error = "Please enter a date"
            return
        }

        // Step 3: Insert into database using our helper method
        val rowId = dbHelper.addOrder(customerId, selectedProduct.id, date)
        
        // Step 4: Check if the insertion was successful
        if (rowId > 0) {
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
            // Close this activity and return to the CustomerDetail screen
            finish()
        } else {
            Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Handles the toolbar back button.
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
