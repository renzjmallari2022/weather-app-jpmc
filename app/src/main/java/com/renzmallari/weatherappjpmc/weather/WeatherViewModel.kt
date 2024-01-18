package com.renzmallari.weatherappjpmc.weather

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.renzmallari.weatherappjpmc.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow<DataState>(DataState.Loading)
    val uiState get() = _uiState.asStateFlow()

    private val _startScreen = mutableStateOf("search")
    val startScreen get() = _startScreen

    fun getWeatherByCityName(name: String) = viewModelScope.launch {
        try {
            _uiState.value = DataState.Success(weatherRepository.getWeatherByCityName(name))
        } catch (e: Exception) {
            _uiState.value = DataState.Failed(errorMessage = e.message!!)
        }
    }

    fun getWeatherByCoordinates(lat: String, lon: String) = viewModelScope.launch {
        try {
            _uiState.value = DataState.Success(weatherRepository.getWeatherByCoordinates(lat, lon))
        } catch (e: Exception) {
            _uiState.value = DataState.Failed(errorMessage = e.message!!)
        }
    }

    fun updateStartScreen(screenName: String) {
        _startScreen.value = screenName
    }
}