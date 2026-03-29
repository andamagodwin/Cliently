package com.example.cliently

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cliently.databinding.ActivityCustomerListBinding

class CustomerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerListBinding
    private lateinit var dbHelper: ClientlyDBHelper
    private lateinit var adapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Cliently"
        supportActionBar?.subtitle = "Customer Directory"

        dbHelper = ClientlyDBHelper(this)

        setupRecyclerView()

        binding.fabAddCustomer.setOnClickListener {
            showAddCustomerDialog()
        }
    }

    override fun onResume() {
        super.onResume()
        loadCustomers()
    }

    private fun setupRecyclerView() {
        binding.rvCustomers.layoutManager = LinearLayoutManager(this)
    }

    private fun loadCustomers() {
        val customers = dbHelper.getAllCustomers()
        adapter = CustomerAdapter(customers) { customer ->
            val intent = Intent(this, CustomerDetailActivity::class.java)
            intent.putExtra("CUSTOMER_ID", customer.id)
            intent.putExtra("CUSTOMER_NAME", customer.name)
            startActivity(intent)
        }
        binding.rvCustomers.adapter = adapter

        // Show/hide empty state
        if (customers.isEmpty()) {
            binding.layoutEmpty.visibility = android.view.View.VISIBLE
            binding.rvCustomers.visibility = android.view.View.GONE
        } else {
            binding.layoutEmpty.visibility = android.view.View.GONE
            binding.rvCustomers.visibility = android.view.View.VISIBLE
        }
        binding.tvCustomerCount.text = "${customers.size} customer${if (customers.size != 1) "s" else ""}"
    }

    private fun showAddCustomerDialog() {
        val dialogBinding = com.example.cliently.databinding.DialogAddCustomerBinding.inflate(layoutInflater)
        val dialog = android.app.AlertDialog.Builder(this)
            .setTitle("Add New Customer")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                val name = dialogBinding.etName.text.toString().trim()
                val phone = dialogBinding.etPhone.text.toString().trim()
                val email = dialogBinding.etEmail.text.toString().trim()
                if (name.isNotEmpty()) {
                    dbHelper.addCustomer(name, phone, email)
                    loadCustomers()
                } else {
                    android.widget.Toast.makeText(this, "Name cannot be empty", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_customer_list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
