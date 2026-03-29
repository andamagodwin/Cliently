package com.example.cliently

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cliently.databinding.ActivityProductListBinding

/**
 * ProductListActivity manages the display of the store's inventory.
 * Requirements: "View Product List," "Delete Product."
 */
class ProductListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductListBinding
    private lateinit var dbHelper: ClientlyDBHelper
    private lateinit var adapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ActionBar with back button
        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Product Inventory"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        dbHelper = ClientlyDBHelper(this)

        setupRecyclerView()

        // FAB to add a new product
        binding.fabAddProduct.setOnClickListener {
            val intent = Intent(this, AddProductActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Always reload products when we return to this screen.
     */
    override fun onResume() {
        super.onResume()
        loadProducts()
    }

    private fun setupRecyclerView() {
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
    }

    /**
     * Fetches products from DB and handles Edit/Delete actions.
     */
    private fun loadProducts() {
        val products = dbHelper.getAllProducts()
        
        adapter = ProductAdapter(
            products = products,
            onEdit = { product ->
                // Navigate to AddProductActivity in "Edit Mode"
                val intent = Intent(this, AddProductActivity::class.java)
                intent.putExtra("PRODUCT_ID", product.id)
                intent.putExtra("PRODUCT_NAME", product.name)
                intent.putExtra("PRODUCT_PRICE", product.price)
                startActivity(intent)
            },
            onDelete = { product ->
                // Show a confirmation dialog before deleting (for safety and UX)
                showDeleteConfirmation(product)
            }
        )
        binding.rvProducts.adapter = adapter

        // Update counts and empty state visibility
        binding.tvProductCount.text = "${products.size} product${if (products.size != 1) "s" else ""}"
        
        if (products.isEmpty()) {
            binding.layoutEmptyProducts.visibility = View.VISIBLE
            binding.rvProducts.visibility = View.GONE
        } else {
            binding.layoutEmptyProducts.visibility = View.GONE
            binding.rvProducts.visibility = View.VISIBLE
        }
    }

    /**
     * Non-functional requirement: UX safety checks (Delete confirmation).
     */
    private fun showDeleteConfirmation(product: Product) {
        AlertDialog.Builder(this)
            .setTitle("Delete Product")
            .setMessage("Are you sure you want to delete '${product.name}'? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                val result = dbHelper.deleteProduct(product.id)
                if (result > 0) {
                    Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show()
                    loadProducts() // Refresh the list
                } else {
                    Toast.makeText(this, "Error deleting product", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Handles the toolbar back button.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
