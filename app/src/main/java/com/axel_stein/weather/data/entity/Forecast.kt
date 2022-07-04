package com.axel_stein.weather.data.entity

import androidx.recyclerview.widget.DiffUtil
import java.time.LocalDate

data class Forecast(
    val id: Long,
    val city: String = "",
    val dateTime: LocalDate = LocalDate.now(),
    val temp: Int = 0,
    val minTemp: Int = 0,
    val maxTemp: Int = 0,
    val iconUrl: String = "",
    val summary: String = "",
    var isCurrent: Boolean = false,
) {
    companion object : DiffUtil.ItemCallback<Forecast>() {
        override fun areItemsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Forecast, newItem: Forecast): Boolean {
            return oldItem == newItem
        }
    }
}