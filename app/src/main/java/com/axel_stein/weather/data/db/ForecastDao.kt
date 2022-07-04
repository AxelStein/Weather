package com.axel_stein.weather.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.axel_stein.weather.data.entity.Forecast

@Dao
interface ForecastDao {
    @Insert
    fun insert(list: Forecast): Long

    @Query("select * from Forecast where city = :city and countryCode = :countryCode order by date")
    fun selectBy(city: String, countryCode: String): List<Forecast>

    @Query("delete from Forecast where city = :city and countryCode = :countryCode")
    fun clearBy(city: String, countryCode: String)
}