package com.axel_stein.weather.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import com.axel_stein.weather.data.entity.Forecast
import java.time.LocalDate
import kotlin.random.Random

class ForecastViewModel : ViewModel() {
    private val _forecasts = MutableLiveData<List<Forecast>>()
    val forecasts = _forecasts as LiveData<List<Forecast>>

    val currentForecast: LiveData<Forecast?> = map(_forecasts) { list ->
        list.firstOrNull { it.isCurrent }
    }

    fun setCity(city: String) {
        val forecasts = mutableListOf<Forecast>()
        val rand = Random(System.currentTimeMillis())
        for (i in 0 until 10) {
            forecasts.add(
                Forecast(
                    id = i.toLong(),
                    city = "City $i",
                    dateTime = LocalDate.now().plusDays(i.toLong()),
                    temp = 18 + i,
                    minTemp = 10 + i,
                    maxTemp = 15 + i,
                    summary = "Cloudy $i",
                    iconUrl = "https://www.weatherbit.io/static/img/icons/t0${rand.nextInt(1, 6)}d.png")
            )
        }
        forecasts[0].isCurrent = true
        _forecasts.value = forecasts
    }

    fun setCurrent(forecast: Forecast) {
        val list = mutableListOf<Forecast>()
        _forecasts.value?.onEach {
            list.add(it.copy(isCurrent = it.id == forecast.id))
        }
        _forecasts.value = list
    }
}