package com.harbornews.weather.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.harbornews.weather.presentation.WeatherViewModel
import com.harbornews.weather.presentation.WeatherViewState
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherWidget(viewModel: WeatherViewModel = koinViewModel()) {
    LaunchedEffect(Unit) { viewModel.refresh() }
    val state by viewModel.state.observeAsState(WeatherViewState.Loading)
    WeatherContent(state)
}

@Composable
fun WeatherContent(state: WeatherViewState) {
    Row(modifier = Modifier.padding(16.dp)) {
        when (state) {
            WeatherViewState.Loading -> Text("Loading weather...")
            WeatherViewState.Unavailable -> Text("Weather unavailable")
            is WeatherViewState.Success -> Text("${state.conditions.temperatureF}°F ${state.conditions.summary}")
        }
    }
}
