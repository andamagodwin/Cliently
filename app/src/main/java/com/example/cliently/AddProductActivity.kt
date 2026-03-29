package com.example.cliently

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cliently.databinding.ActivityAddProductBinding

/**
 * AddProductActivity handles both creation and editing of products.
 * Requirements: "Add Product," "Edit Product."
 */
class AddProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddProductBinding
    private lateinit var dbHelper: ClientlyDBHelper
    private var productId: Int = -1 // If -1, we are in "Add Mode". Otherwise, "Edit Mode".

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = ClientlyDBHelper(this)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Check if we are in "Edit Mode" based on intent extras
        productId = intent.getIntExtra("PRODUCT_ID", -1)
        if (productId != -1) {
            setupEditMode()
        } else {
            setupAddMode()
        }

        binding.btnSaveProduct.setOnClickListener {
            saveProduct()
        }
    }

    /**
     * Set up UI for adding a new product.
     */
    private fun setupAddMode() {
        supportActionBar?.title = "Add New Product"
        binding.tvFormTitle.text = "Add Product"
        binding.btnSaveProduct.text = "Create Product"
    }

    /**
     * Set up UI for editing an existing product.
     */
    private fun setupEditMode() {
        supportActionBar?.title = "Edit Product"
        binding.tvFormTitle.text = "Update Details"
        binding.btnSaveProduct.text = "Update Product"

        // Pre-fill the form with existing data
        val name = intent.getStringExtra("PRODUCT_NAME") ?: ""
        val price = intent.getDoubleExtra("PRODUCT_PRICE", 0.0)

        binding.etProductName.setText(name)
        binding.etProductPrice.setText(price.toString())
    }

    /**
     * Handles the database save operation (Insert or Update).
     */
    private fun saveProduct() {
        val name = binding.etProductName.text.toString().trim()
        val priceStr = binding.etProductPrice.text.toString().trim()

        // Step 1: Validation
        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please enter both name and price", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceStr.toDoubleOrNull()
        if (price == null || price < 0) {
            binding.etProductPrice.error = "Invalid price"
            return
        }

        // Step 2: Database operation
        val result: Long
        if (productId == -1) {
            // INSERT Mode
            result = dbHelper.writableDatabase.insert(
                ClientlyDBHelper.TABLE_PRODUCTS,
                null,
                android.content.ContentValues().apply {
                    put(ClientlyDBHelper.COL_PROD_NAME, name)
                    put(ClientlyDBHelper.COL_PROD_PRICE, price)
                }
            )
            if (result > 0) {
                Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to add product", Toast.LENGTH_SHORT).show()
            }
        } else {
            // UPDATE Mode
            val rowsAffected = dbHelper.updateProduct(productId, name, price)
            if (rowsAffected > 0) {
                Toast.makeText(this, "Product updated successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Failed to update product", Toast.LENGTH_SHORT).show()
            }
        }
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
