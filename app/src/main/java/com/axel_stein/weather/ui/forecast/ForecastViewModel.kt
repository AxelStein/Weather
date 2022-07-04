package com.axel_stein.weather.ui.forecast

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations.map
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.axel_stein.weather.data.entity.Forecast
import com.axel_stein.weather.domain.GetForecastUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastViewModel @Inject constructor(private val getForecast: GetForecastUseCase) : ViewModel() {

    private val _forecasts = MutableLiveData<List<Forecast>>()
    val forecasts = _forecasts as LiveData<List<Forecast>>

    val currentForecast: LiveData<Forecast?> = map(_forecasts) { list ->
        list.firstOrNull { it.isCurrent }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading as LiveData<Boolean>

    private val _isErrorShown = MutableLiveData<Boolean>()
    val isErrorShown = _isErrorShown as LiveData<Boolean>

    private val _isNoDataShown = MutableLiveData<Boolean>()
    val isNoDataShown = _isNoDataShown as LiveData<Boolean>

    private val _isConnectionErrorShown = MutableLiveData<Boolean>()
    val isConnectionErrorShown = _isConnectionErrorShown as LiveData<Boolean>

    private var city = ""
    private var countryCode = ""

    fun setCity(city: String, countryCode: String) {
        if (this.city.contentEquals(city) && this.countryCode.contentEquals(countryCode)) return
        this.city = city
        this.countryCode = countryCode

        viewModelScope.launch {
            _isLoading.value = true

            val result = getForecast(city, countryCode)

            _isLoading.postValue(false)

            if (result.noData) {
                _isNoDataShown.value = true
            }
            if (result.isFailure && !result.isConnectionError) {
                _isErrorShown.value = true
            } else {
                _isConnectionErrorShown.value = result.isConnectionError && !result.noData
                _forecasts.value = result.list.apply {
                    if (isNotEmpty()) {
                        first().isCurrent = true
                    }
                }
            }
        }
    }

    fun setCurrent(forecast: Forecast) {
        val list = mutableListOf<Forecast>()
        _forecasts.value?.onEach { item ->
            list.add(
                item.copy(isCurrent = item.id == forecast.id)
            )
        }
        _forecasts.value = list
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}