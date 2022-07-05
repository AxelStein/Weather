package com.axel_stein.weather.domain

import com.axel_stein.weather.data.db.ForecastDao
import com.axel_stein.weather.data.entity.Forecast
import com.axel_stein.weather.data.service.WeatherService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class GetForecastUseCase(
    private val weatherService: WeatherService,
    private val dao: ForecastDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    suspend operator fun invoke(
        city: String,
        countryCode: String,
        days: Int = 10,
    ) = withContext(dispatcher) {
        var error: Exception? = null

        val list = try {
            fetchFromService(city, countryCode, days)
        } catch (e: Exception) {
            error = e
            dao.selectBy(city, countryCode)
        }

        Result(list, error)
    }

    private suspend fun fetchFromService(
        city: String,
        countryCode: String,
        days: Int
    ): List<Forecast> {
        val response = weatherService.fetchDailyForecast(city, countryCode, days)
        return if (response.isSuccessful) {
            val list = response.body()
            if (list.isNullOrEmpty()) {
                throw Exception("List is empty")
            } else {
                dao.clearBy(city, countryCode)
                list.onEach { forecast ->
                    forecast.city = city
                    forecast.countryCode = countryCode
                    forecast.id = dao.insert(forecast)
                }
            }
            list
        } else {
            throw Exception(response.errorBody()?.string())
        }
    }

    data class Result(
        val list: List<Forecast>,
        val error: Exception?,
    ) {
        val noData: Boolean
            get() = list.isEmpty()

        val isSuccess: Boolean
            get() = error == null

        val isFailure: Boolean
            get() = error != null

        val isConnectionError: Boolean
            get() = error != null && (error is UnknownHostException || error is ConnectException || error is SocketTimeoutException)
    }
}