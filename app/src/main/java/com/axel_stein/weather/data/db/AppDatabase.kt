package com.axel_stein.weather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.axel_stein.weather.data.entity.Forecast

@Database(
    entities = [
        Forecast::class
    ],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun forecastDao(): ForecastDao
}