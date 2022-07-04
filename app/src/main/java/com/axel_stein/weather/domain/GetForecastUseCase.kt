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
            val response = weatherService.fetchDailyForecast(city, countryCode, days)
            if (response.isSuccessful) {
                val list = response.body()
                if (list.isNullOrEmpty()) {
                    throw Exception("List is empty")
                } else {
                    dao.clearBy(city, countryCode)
                    list.onEach {
                        it.city = city
                        it.countryCode = countryCode
                        it.id = dao.insert(it)
                    }
                }
                list
            } else {
                throw Exception(response.errorBody()?.string())
            }
        } catch (e: Exception) {
            error = e
            e.printStackTrace()

            dao.selectBy(city, countryCode)
        }

        Result(list, error)
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