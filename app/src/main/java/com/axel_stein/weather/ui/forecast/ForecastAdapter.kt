package com.axel_stein.weather.ui.forecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.axel_stein.weather.R
import com.axel_stein.weather.data.entity.Forecast
import com.axel_stein.weather.databinding.ItemForecastBinding
import com.bumptech.glide.Glide
import java.time.format.TextStyle
import java.util.*

class ForecastAdapter : ListAdapter<Forecast, ForecastAdapter.ViewHolder>(Forecast.Companion) {
    var onItemClickListener: ((Forecast) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemForecastBinding.inflate(inflater, parent, false)
        return ViewHolder(binding).apply {
            itemView.setOnClickListener {
                val pos = adapterPosition
                if (pos in 0 until itemCount) {
                    onItemClickListener?.invoke(getItem(pos))
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.setItem(getItem(position))
    }

    class ViewHolder(private val binding: ItemForecastBinding) : RecyclerView.ViewHolder(binding.root) {
        private val context = binding.root.context

        fun setItem(forecast: Forecast) = with(binding) {
            root.isActivated = forecast.isCurrent

            minTemp.text = context.getString(R.string.temp, forecast.minTemp)
            maxTemp.text = context.getString(R.string.temp, forecast.maxTemp)
            weekDay.text = forecast.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ROOT)

            Glide.with(icon)
                .load(forecast.iconUrl)
                .error(R.drawable.ic_error_24dp)
                .into(icon)
        }
    }
}