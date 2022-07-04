package com.axel_stein.weather.data

import android.content.Context
import androidx.room.Room
import com.axel_stein.weather.data.db.AppDatabase
import com.axel_stein.weather.data.db.ForecastDao
import com.axel_stein.weather.data.entity.Forecast
import com.axel_stein.weather.data.service.ForecastListDeserializer
import com.axel_stein.weather.data.service.WeatherService
import com.axel_stein.weather.data.service.apiKey
import com.axel_stein.weather.domain.GetForecastUseCase
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun weatherService(): WeatherService {
        val client = OkHttpClient
            .Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()

                val url = originalRequest.url
                    .newBuilder()
                    .addQueryParameter("key", apiKey)
                    .build()

                val request = originalRequest
                    .newBuilder()
                    .url(url)
                    .build()

                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

        // val token = object : TypeToken<List<Forecast>>() {}.type
        val type = TypeToken.getParameterized(List::class.java, Forecast::class.java).type
        val gson = GsonBuilder()
            .registerTypeAdapter(type, ForecastListDeserializer())
            .create()

        return Retrofit
            .Builder()
            .baseUrl("https://api.weatherbit.io/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
            .create(WeatherService::class.java)
    }

    @Singleton
    @Provides
    fun appDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room
            .databaseBuilder(context, AppDatabase::class.java, context.packageName)
            .build()
    }

    @Singleton
    @Provides
    fun getForecastDao(db: AppDatabase) = db.forecastDao()

    @Provides
    fun getForecastUseCase(
        service: WeatherService,
        dao: ForecastDao,
    ) = GetForecastUseCase(service, dao)
}