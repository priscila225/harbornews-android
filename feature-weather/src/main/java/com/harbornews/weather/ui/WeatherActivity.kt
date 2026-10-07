package com.harbornews.weather.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.harbornews.weather.databinding.ActivityWeatherBinding
import com.harbornews.weather.domain.model.Conditions
import com.harbornews.weather.presentation.WeatherNavEvent
import com.harbornews.weather.presentation.WeatherViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherActivity : AppCompatActivity() {
    private val viewModel: WeatherViewModel by viewModel()
    private lateinit var binding: ActivityWeatherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.hourly.setContent { HourlyRow() }
        binding.alertsButton.setOnClickListener { viewModel.onAlertsClicked() }

        lifecycleScope.launch {
            viewModel.conditions.collect { render(it) }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigation.collect { event ->
                    when (event) {
                        WeatherNavEvent.OpenAlerts -> Toast.makeText(this@WeatherActivity, "Alerts", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        viewModel.refresh()
    }

    private fun render(conditions: Conditions?) {
        binding.conditions.text = conditions?.let { "${it.temperatureF}°F ${it.summary}" } ?: "No reading yet"
    }
}
