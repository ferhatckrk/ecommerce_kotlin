package com.example.ecommercekotlin.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.ecommercekotlin.data.Product
import com.example.ecommercekotlin.databinding.SpecialRvItemBinding

class SpecialProductsAdapter :
    RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {
    inner class SpecialProductsViewHolder(private val binding: SpecialRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    val diffCallBack = object : DiffUtil.ItemCallback<Product>() {

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            TODO("Not yet implemented")
        }

    }


}