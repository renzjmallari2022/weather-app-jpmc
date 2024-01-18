package com.renzmallari.weatherappjpmc.weather

import com.renzmallari.weatherappjpmc.network.WeatherApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val weatherApiService: WeatherApiService) {

    suspend fun getWeatherByCityName(name: String) = weatherApiService.getWeatherByCityName(name)

    suspend fun getWeatherByCoordinates(lat: String, lon: String) =
        weatherApiService.getWeatherByCoordinates(lat, lon)
}