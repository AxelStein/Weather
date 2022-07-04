package com.axel_stein.weather.data.service

import com.axel_stein.weather.data.entity.Forecast
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/v2.0/forecast/daily")
    suspend fun fetchDailyForecast(
        @Query("city") city: String,
        @Query("country") countryCode: String,
        @Query("days") days: Int = 10,
    ): Response<List<Forecast>>
}