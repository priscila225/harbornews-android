package com.harbornews.weather.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.harbornews.weather.presentation.WeatherViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun HourlyRow(viewModel: WeatherViewModel = koinViewModel()) {
    viewModel.loadHourly()
    val hourly by viewModel.hourly.observeAsState(emptyList())
    Row(modifier = Modifier.padding(16.dp)) {
        hourly.forEach { entry ->
            Text("${entry.hour}:00 ${entry.temperatureF}°  ", modifier = Modifier.padding(end = 8.dp))
        }
    }
}
