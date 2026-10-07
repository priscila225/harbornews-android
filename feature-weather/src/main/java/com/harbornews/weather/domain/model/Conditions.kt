package com.harbornews.weather.domain.model

data class Conditions(
    val stationId: String,
    val temperatureF: Int,
    val summary: String,
    val updatedAtMillis: Long,
)
