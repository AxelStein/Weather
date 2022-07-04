package com.axel_stein.weather.ui.locations

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.axel_stein.weather.data.entity.Location

class LocationsViewModel : ViewModel() {
    private val _locations = MutableLiveData<List<Location>>()
    val locations = _locations as LiveData<List<Location>>

    init {
        _locations.value = listOf(
            Location(1, "Kyiv", "UA"),
            Location(2, "London", "GB"),
        )
    }
}