package com.renzmallari.weatherappjpmc

import com.renzmallari.weatherappjpmc.util.Constants
import com.renzmallari.weatherappjpmc.weather.WeatherRepository
import com.renzmallari.weatherappjpmc.weather.WeatherViewModel
import io.mockk.MockKAnnotations
import io.mockk.MockKSettings
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.unmockkAll
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class WeatherViewModelTest {
    private lateinit var viewModel: WeatherViewModel

    @MockK
    lateinit var weatherRepository: WeatherRepository

    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(
            this,
            relaxUnitFun = MockKSettings.relaxUnitFun,
            relaxed = MockKSettings.relaxed
        )
        viewModel = spyk(WeatherViewModel(weatherRepository = weatherRepository))
    }

    @After
    fun cleanup() {
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `verified that get weather by city name called exactly once`() = runTest {
        val weatherResponse = mockk<Job>()
        coEvery { viewModel.getWeatherByCityName(any()) } returns weatherResponse
        viewModel.getWeatherByCityName("New York")
        coVerify(exactly = 1) { viewModel.getWeatherByCityName(any()) }
    }

    @Test
    fun `verified that get weather by coordinates called exactly once`() = runTest {
        val weatherResponse = mockk<Job>()
        coEvery { viewModel.getWeatherByCoordinates(any(), any()) } returns weatherResponse
        viewModel.getWeatherByCoordinates(
            Constants.DEFAULT_COORDINATES,
            Constants.DEFAULT_COORDINATES
        )
        coVerify(exactly = 1) {
            viewModel.getWeatherByCoordinates(
                Constants.DEFAULT_COORDINATES,
                Constants.DEFAULT_COORDINATES
            )
        }
    }
}