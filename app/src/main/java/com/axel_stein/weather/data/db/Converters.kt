package com.axel_stein.weather.data.db

import androidx.room.TypeConverter
import java.time.LocalDate

class Converters {
    @TypeConverter
    fun dateToStr(date: LocalDate): String = date.toString()

    @TypeConverter
    fun strToDate(str: String): LocalDate = LocalDate.parse(str)
}