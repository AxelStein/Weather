package com.axel_stein.weather.ui.locations

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.axel_stein.weather.data.entity.Location
import com.axel_stein.weather.databinding.ItemLocationBinding

class LocationsAdapter : ListAdapter<Location, LocationsAdapter.ViewHolder>(Location.Companion) {
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
        holder.setItem(getItem(position))
    }

    class ViewHolder(private val binding: ItemLocationBinding) : RecyclerView.ViewHolder(binding.root) {

        fun setItem(item: Location) {
            binding.city.text = item.city
        }
    }
}