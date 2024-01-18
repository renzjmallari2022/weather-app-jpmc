package com.renzmallari.weatherappjpmc.util

import com.renzmallari.weatherappjpmc.models.WeatherResponse

sealed class DataState {
    class Success(val data: WeatherResponse) : DataState()
    class Failed(val errorMessage: String) : DataState()
    object Loading : DataState()
}