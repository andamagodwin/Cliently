package com.example.cliently

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cliently.databinding.ActivityCustomerDetailBinding

class CustomerDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerDetailBinding
    private lateinit var dbHelper: ClientlyDBHelper
    private var customerId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        customerId = intent.getIntExtra("CUSTOMER_ID", -1)
        val customerName = intent.getStringExtra("CUSTOMER_NAME") ?: "Customer"

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = customerName
        supportActionBar?.subtitle = "Customer Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = ClientlyDBHelper(this)

        binding.rvOrders.layoutManager = LinearLayoutManager(this)

        binding.fabAddOrder.setOnClickListener {
            val intent = Intent(this, AddOrderActivity::class.java)
            intent.putExtra("CUSTOMER_ID", customerId)
            intent.putExtra("CUSTOMER_NAME", customerName)
            startActivity(intent)
        }

        loadCustomerDetails()
    }

    override fun onResume() {
        super.onResume()
        loadOrderHistory()
    }

    private fun loadCustomerDetails() {
        val customer = dbHelper.getCustomerById(customerId) ?: return

        val initials = customer.name.split(" ")
            .mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
            .take(2).joinToString("")
        binding.tvDetailAvatar.text = initials
        binding.tvDetailName.text = customer.name
        binding.tvDetailPhone.text = customer.phone.ifEmpty { "No phone on record" }
        binding.tvDetailEmail.text = customer.email.ifEmpty { "No email on record" }
    }

    private fun loadOrderHistory() {
        val orders = dbHelper.getOrdersByCustomer(customerId)
        val adapter = OrderAdapter(orders)
        binding.rvOrders.adapter = adapter

        binding.tvOrderCount.text = "${orders.size} order${if (orders.size != 1) "s" else ""}"

        if (orders.isEmpty()) {
            binding.layoutNoOrders.visibility = android.view.View.VISIBLE
            binding.rvOrders.visibility = android.view.View.GONE
        } else {
            binding.layoutNoOrders.visibility = android.view.View.GONE
            binding.rvOrders.visibility = android.view.View.VISIBLE
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
