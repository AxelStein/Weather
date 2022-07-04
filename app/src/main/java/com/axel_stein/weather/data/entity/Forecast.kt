package com.axel_stein.weather.data.entity

import androidx.recyclerview.widget.DiffUtil
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity
data class Forecast(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    var city: String = "",
    var countryCode: String = "",
    var date: LocalDate = LocalDate.now(),
    var temp: Int = 0,
    var minTemp: Int = 0,
    var maxTemp: Int = 0,
    var iconUrl: String = "",
    var summary: String = "",

    @Ignore
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