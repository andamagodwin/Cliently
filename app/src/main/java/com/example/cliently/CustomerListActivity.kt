package com.example.cliently

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cliently.databinding.ActivityCustomerListBinding
import com.example.cliently.databinding.DialogAddCustomerBinding

/**
 * CustomerListActivity displays all customers in a vertical list.
 * It uses a RecyclerView for performance and efficiency.
 */
class CustomerListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerListBinding
    private lateinit var dbHelper: ClientlyDBHelper
    private lateinit var adapter: CustomerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup the top ActionBar/Toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Cliently"
        supportActionBar?.subtitle = "Customer Directory"

        dbHelper = ClientlyDBHelper(this)

        // Initial setup for the list (empty until we load data)
        setupRecyclerView()

        // Floating Action Button (FAB) to open the "Add Customer" dialog
        binding.fabAddCustomer.setOnClickListener {
            showAddCustomerDialog()
        }
    }

    /**
     * onResume is called when the user returns to this screen.
     * We reload the data here to ensure changes (like a new customer) are visible.
     */
    override fun onResume() {
        super.onResume()
        loadCustomers()
    }

    /**
     * Tells the RecyclerView how to arrange its items.
     * LinearLayoutManager makes it scroll vertically.
     */
    private fun setupRecyclerView() {
        binding.rvCustomers.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Fetches all customers from the database and gives them to the adapter.
     */
    private fun loadCustomers() {
        // Step 1: Query the database
        val customers = dbHelper.getAllCustomers()
        
        // Step 2: Initialize the Adapter
        // The second parameter is an "OnClickListener" lambda function:
        // when a customer is tapped, it opens their detail page.
        adapter = CustomerAdapter(customers) { customer ->
            val intent = Intent(this, CustomerDetailActivity::class.java)
            intent.putExtra("CUSTOMER_ID", customer.id) // Pass ID to the next screen
            intent.putExtra("CUSTOMER_NAME", customer.name)
            startActivity(intent)
        }
        
        // Step 3: Bind the adapter to the RecyclerView
        binding.rvCustomers.adapter = adapter

        // Step 4: Handle "Empty State" - show a message if there's no data
        if (customers.isEmpty()) {
            binding.layoutEmpty.visibility = View.VISIBLE
            binding.rvCustomers.visibility = View.GONE
        } else {
            binding.layoutEmpty.visibility = View.GONE
            binding.rvCustomers.visibility = View.VISIBLE
        }
        
        // Update the count text in the toolbar
        binding.tvCustomerCount.text = "${customers.size} customer${if (customers.size != 1) "s" else ""}"
    }

    /**
     * Displays an Alert Dialog with custom XML to quickly add a new customer.
     */
    private fun showAddCustomerDialog() {
        // Inflate the special dialog layout
        val dialogBinding = DialogAddCustomerBinding.inflate(layoutInflater)
        
        // Build the dialog window
        val dialog = AlertDialog.Builder(this)
            .setTitle("Add New Customer")
            .setView(dialogBinding.root)
            .setPositiveButton("Add") { _, _ ->
                // This code runs when the user taps "Add"
                val name = dialogBinding.etName.text.toString().trim()
                val phone = dialogBinding.etPhone.text.toString().trim()
                val email = dialogBinding.etEmail.text.toString().trim()
                
                if (name.isNotEmpty()) {
                    // Save to database
                    dbHelper.addCustomer(name, phone, email)
                    // Refresh the list immediately
                    loadCustomers()
                } else {
                    Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null) // "Cancel" just closes the dialog
            .create()
            
        dialog.show()
    }

    /**
     * Inflate the menu XML file into the top ActionBar.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_customer_list, menu)
        return true
    }

    /**
     * Define what happens when a menu item is clicked.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                // Exit this screen (returns the user to the LoginActivity)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
