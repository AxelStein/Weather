package com.axel_stein.weather.ui.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.axel_stein.weather.databinding.ItemLocationBinding

class LocationsAdapter : RecyclerView.Adapter<LocationsAdapter.ViewHolder>() {
    var onItemClickListener: (() -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnClickListener {
                onItemClickListener?.invoke()
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    override fun getItemCount() = 10

    class ViewHolder(private val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {}
}