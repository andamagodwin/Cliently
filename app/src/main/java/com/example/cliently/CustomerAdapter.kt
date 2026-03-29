package com.example.cliently

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cliently.databinding.ItemCustomerBinding

/**
 * CustomerAdapter connects our list of Customer data to the RecyclerView UI.
 * It "adapts" each data object into a visual row (ItemView).
 * 
 * @param customers - The list of customers from the database.
 * @param onItemClick - A function (lambda) that runs when a row is tapped.
 */
class CustomerAdapter(
    private val customers: List<Customer>,
    private val onItemClick: (Customer) -> Unit
) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    /**
     * ViewHolder holds "references" to the UI views inside a single row (item_customer.xml).
     * This avoids repeated calls to findViewById and makes the list scroll smoothly.
     */
    inner class CustomerViewHolder(private val binding: ItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * The bind() method maps our data model's fields to the XML views.
         */
        fun bind(customer: Customer) {
            // Update the text fields
            binding.tvCustomerName.text = customer.name
            binding.tvCustomerPhone.text = customer.phone
            binding.tvCustomerEmail.text = customer.email

            // ── GENERATE INITIALS ──
            // We split the name by spaces and grab the first letter of each part to create an avatar.
            // Example: "Carol Auma" -> ["C", "A"] -> "CA"
            val initials = customer.name.split(" ")
                .mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
                .take(2).joinToString("")
            binding.tvAvatar.text = initials

            // Handle clicking the entire row
            binding.root.setOnClickListener { onItemClick(customer) }
        }
    }

    /**
     * Called by RecyclerView to create a NEW row layout (the ViewHolder).
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        // Inflate the item_customer.xml layout
        val binding = ItemCustomerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CustomerViewHolder(binding)
    }

    /**
     * Called by RecyclerView to BIND existing view holders to a specific data object by position.
     */
    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(customers[position])
    }

    /**
     * Returns the total list size.
     */
    override fun getItemCount() = customers.size
}
