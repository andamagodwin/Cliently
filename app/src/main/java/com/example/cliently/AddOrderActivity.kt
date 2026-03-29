package com.example.cliently

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cliently.databinding.ActivityAddOrderBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddOrderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddOrderBinding
    private lateinit var dbHelper: ClientlyDBHelper
    private var customerId: Int = -1
    private var products: List<Product> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customerId = intent.getIntExtra("CUSTOMER_ID", -1)
        val customerName = intent.getStringExtra("CUSTOMER_NAME") ?: "Customer"

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "New Order"
        supportActionBar?.subtitle = "For $customerName"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = ClientlyDBHelper(this)

        // Pre-fill today's date
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        binding.etOrderDate.setText(today)

        loadProducts()

        binding.btnPlaceOrder.setOnClickListener {
            placeOrder()
        }
    }

    private fun loadProducts() {
        products = dbHelper.getAllProducts()
        val productNames = products.map { "${it.name} — ${"$"}${"%.2f".format(it.price)}" }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, productNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProduct.adapter = adapter
    }

    private fun placeOrder() {
        if (products.isEmpty()) {
            Toast.makeText(this, "No products available", Toast.LENGTH_SHORT).show()
            return
        }

        val selectedIndex = binding.spinnerProduct.selectedItemPosition
        val selectedProduct = products[selectedIndex]
        val date = binding.etOrderDate.text.toString().trim()

        if (date.isEmpty()) {
            binding.etOrderDate.error = "Please enter a date"
            return
        }

        val rowId = dbHelper.addOrder(customerId, selectedProduct.id, date)
        if (rowId > 0) {
            Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Failed to place order. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
