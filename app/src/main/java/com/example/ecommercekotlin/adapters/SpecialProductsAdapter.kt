package com.example.ecommercekotlin.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ecommercekotlin.data.Product
import com.example.ecommercekotlin.databinding.SpecialRvItemBinding

class SpecialProductsAdapter :
    RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {
    inner class SpecialProductsViewHolder(private val binding: SpecialRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(product: Product) {
                binding.apply {
                    Glide.with(itemView).load(product.images[0]).into(imageSpecialRvItem)
                    tvSpecialProductName.text=product.name
                    tvSpecialProductPrice.text=product.price.toString()

                }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<Product>() {

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem  == newItem
        }

        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

    }

      val differ = AsyncListDiffer(this, diffCallBack)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        return SpecialProductsViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        val product  = differ.currentList[position]
        holder.bind(product)
    }

}