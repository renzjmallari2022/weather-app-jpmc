package com.renzmallari.weatherappjpmc.weather

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.renzmallari.weatherappjpmc.composables.FailedScreen
import com.renzmallari.weatherappjpmc.composables.ItemList
import com.renzmallari.weatherappjpmc.composables.LoadingScreen
import com.renzmallari.weatherappjpmc.composables.SearchView
import com.renzmallari.weatherappjpmc.models.WeatherResponse
import com.renzmallari.weatherappjpmc.util.Constants
import com.renzmallari.weatherappjpmc.util.DataState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeatherFragment : Fragment() {

    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var fusedLocations: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                Main(viewModel)
            }
        }
    }


    @Composable
    fun Main(
        viewModel: WeatherViewModel
    ) {
        val navController = rememberNavController()
        var latState by remember {
            mutableStateOf("0.00")
        }
        var lonState by remember {
            mutableStateOf("0.00")
        }
        val context = LocalContext.current
        // permission launcher
        val requestPermission = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                if (isGranted) {
                    getCurrentLocation(context) { lat, lon ->
                        latState = lat.toString()
                        lonState = lon.toString()
                        viewModel.getWeatherByCoordinates(latState, lonState)
                    }
                    navController.navigate("info")
                }
            }
        )
        LaunchedEffect(Unit) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (hasLocationPermission(context)) {
            viewModel.updateStartScreen("info")
        } else {
            viewModel.updateStartScreen("search")
        }

        NavHost(navController = navController, startDestination = viewModel.startScreen.value) {
            composable("search") {
                SearchScreen(
                    navController,
                    viewModel
                )
            }
            composable("info") {
                InfoScreen(
                    navController,
                    viewModel
                )
            }
        }
    }

    private fun hasLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(
        context: Context,
        callback: (Double, Double) -> Unit
    ) {
        val fusedLocation = LocationServices.getFusedLocationProviderClient(context)

        fusedLocation
            .lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    callback(location.latitude, location.longitude)
                }
            }
            .addOnFailureListener { e ->
                e.stackTrace
            }
    }

    @Composable
    fun SearchScreen(
        navController: NavController,
        viewModel: WeatherViewModel
    ) {
        val textState = remember { mutableStateOf(TextFieldValue("")) }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Search Your City below to check the Weather.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            SearchView(
                state = textState,
                viewModel = viewModel
            )
            if (textState.value.text != "") {
                ItemList(
                    state = textState,
                    viewModel = viewModel,
                    navController = navController
                )
            }

        }

    }

    @Composable
    fun InfoScreen(
        navController: NavController,
        viewModel: WeatherViewModel,
    ) {
        val dataState = viewModel.uiState.collectAsState()
        val isStartScreen = viewModel.startScreen.value == "info"
        val alignmentButton = if(isStartScreen) Alignment.CenterHorizontally else Alignment.Start

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = alignmentButton
        ) {
            if (isStartScreen) {
                Button(onClick = { navController.navigate("search") }) {
                    Text(text = "Search Location with US city name")
                }
            } else {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }

            when (dataState.value) {
                is DataState.Success -> {
                    SuccessScreen(data = (dataState.value as DataState.Success).data)
                }

                is DataState.Failed -> {
                    FailedScreen(message = (dataState.value as DataState.Failed).errorMessage)
                }

                is DataState.Loading -> {
                    LoadingScreen()
                }
            }
        }
    }

    @Composable
    fun SuccessScreen(data: WeatherResponse) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            data.let {
                val iconUrl = Constants.ICON_URL.replace("url", it.weather.first().icon)
                Text(
                    text = it.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 32.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Image(
                    painter = rememberAsyncImagePainter(iconUrl),
                    contentDescription = null,
                    modifier = Modifier.size(128.dp)
                )
                Text(
                    text = it.weather.first().description,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

