package com.axel_stein.weather.data.entity

import androidx.recyclerview.widget.DiffUtil

data class Location(
    val id: Long,
    val city: String,
    val countryCode: String,
) {
    companion object : DiffUtil.ItemCallback<Location>() {
        override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
            return oldItem == newItem
        }
    }
}