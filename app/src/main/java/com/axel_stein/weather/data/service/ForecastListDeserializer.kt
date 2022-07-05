package com.axel_stein.weather.data.service

import com.axel_stein.weather.data.entity.Forecast
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import java.lang.reflect.Type
import java.time.LocalDate

class ForecastListDeserializer : JsonDeserializer<List<Forecast>> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): List<Forecast> {
        val result = mutableListOf<Forecast>()

        json?.asJsonObject?.let {
            val arr = it.getAsJsonArray("data")

            arr.forEach { jsonElement ->
                val obj = jsonElement.asJsonObject
                val weather = obj.get("weather").asJsonObject

                result.add(
                    Forecast(
                        date = LocalDate.parse(obj.optString("datetime")),
                        temp = obj.optFloat("temp").toInt(),
                        minTemp = obj.optFloat("low_temp").toInt(),
                        maxTemp = obj.optFloat("max_temp").toInt(),
                        iconUrl = weather.optIconUrl("icon"),
                        summary = weather.optString("description"),
                    )
                )
            }
        }

        return result
    }

    private fun JsonObject.optIconUrl(memberName: String): String {
        return "https://www.weatherbit.io/static/img/icons/${optString(memberName)}.png"
    }

    private fun JsonObject.optString(memberName: String): String {
        return if (has(memberName)) get(memberName).asString else ""
    }

    private fun JsonObject.optFloat(memberName: String): Float {
        return if (has(memberName)) get(memberName).asFloat else 0f
    }
}