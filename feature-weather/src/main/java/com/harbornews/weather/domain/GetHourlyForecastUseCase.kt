package com.harbornews.weather.domain

import com.harbornews.common.Result
import com.harbornews.weather.domain.model.HourlyEntry

class GetHourlyForecastUseCase(private val repository: WeatherRepository) {
    suspend fun execute(): Result<List<HourlyEntry>> = repository.hourly()
}
