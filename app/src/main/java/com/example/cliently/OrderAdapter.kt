package com.example.cliently

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cliently.databinding.ItemOrderBinding
import java.text.NumberFormat
import java.util.Locale

/**
 * OrderAdapter manages the display of past transactions in a list (RecyclerView).
 * It presents details about the products purchased by a customer.
 */
class OrderAdapter(
    private val orders: List<Order>
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {

    /**
     * ViewHolder maps each order object to its XML item layout (item_order.xml).
     */
    inner class OrderViewHolder(private val binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        /**
         * The bind() method populates the list row with formatted data.
         */
        fun bind(order: Order, position: Int) {
            // Step 1: Format Order ID with padding zeros (Ex: #001, #002)
            binding.tvOrderNumber.text = "#${String.format("%03d", order.id)}"
            
            // Step 2: Show the Product Name
            binding.tvProductName.text = order.productName
            
            // Step 3: Show the Date string exactly as stored in DB
            binding.tvOrderDate.text = order.date
            
            // Step 4: Format the price using currency standards (US Locale for "$")
            val formatter = NumberFormat.getCurrencyInstance(Locale.US)
            binding.tvOrderPrice.text = formatter.format(order.productPrice)
        }
    }

    /**
     * Boilerplate: Creates the visual layout from item_order.xml.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return OrderViewHolder(binding)
    }

    /**
     * Boilerplate: Binds a specific position in the data list to the view.
     */
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(orders[position], position)
    }

    /**
     * Boilerplate: Returns our total count of orders.
     */
    override fun getItemCount() = orders.size
}
