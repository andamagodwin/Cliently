package com.example.cliently

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cliently.databinding.ItemProductBinding
import java.text.NumberFormat
import java.util.Locale

/**
 * ProductAdapter displays the store's inventory items.
 * It provides "Edit" and "Delete" actions for each product.
 */
class ProductAdapter(
    private val products: List<Product>,
    private val onEdit: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    /**
     * ViewHolder for product items.
     */
    inner class ProductViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * Binds product data to the UI components.
         */
        fun bind(product: Product) {
            binding.tvItemProductName.text = product.name
            
            // Format price as currency
            val formatter = NumberFormat.getCurrencyInstance(Locale.US)
            binding.tvItemProductPrice.text = formatter.format(product.price)

            // Generate a simple initial for the avatar
            binding.tvProductInitial.text = product.name.firstOrNull()?.uppercaseChar()?.toString() ?: "P"

            // Setup button click listeners
            binding.btnEditProduct.setOnClickListener { onEdit(product) }
            binding.btnDeleteProduct.setOnClickListener { onDelete(product) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount() = products.size
}
