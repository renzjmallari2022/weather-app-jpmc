package com.renzmallari.weatherappjpmc.network

import com.renzmallari.weatherappjpmc.BuildConfig
import com.renzmallari.weatherappjpmc.models.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("weather")
    suspend fun getWeatherByCityName(
        @Query("q") cityName: String,
        @Query("appid") key: String = BuildConfig.API_KEY,
    ): WeatherResponse


    @GET("weather")
    suspend fun getWeatherByCoordinates(
        @Query("lat") lat: String,
        @Query("lon") lon: String,
        @Query("appid") key: String = BuildConfig.API_KEY
    ): WeatherResponse
}