package com.example.cliently

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cliently.databinding.ItemCustomerBinding

class CustomerAdapter(
    private val customers: List<Customer>,
    private val onItemClick: (Customer) -> Unit
) : RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder>() {

    inner class CustomerViewHolder(private val binding: ItemCustomerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(customer: Customer) {
            binding.tvCustomerName.text = customer.name
            binding.tvCustomerPhone.text = customer.phone
            binding.tvCustomerEmail.text = customer.email

            // Initials avatar
            val initials = customer.name.split(" ")
                .mapNotNull { it.firstOrNull()?.uppercaseChar()?.toString() }
                .take(2).joinToString("")
            binding.tvAvatar.text = initials

            binding.root.setOnClickListener { onItemClick(customer) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerViewHolder {
        val binding = ItemCustomerBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CustomerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomerViewHolder, position: Int) {
        holder.bind(customers[position])
    }

    override fun getItemCount() = customers.size
}
