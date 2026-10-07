package com.harbornews.weather.presentation

import com.harbornews.weather.domain.model.Conditions

sealed class WeatherViewState {
    data object Loading : WeatherViewState()

    data class Success(val conditions: Conditions) : WeatherViewState()

    data object Unavailable : WeatherViewState()
}
