package com.harbornews.weather.domain

import com.harbornews.weather.domain.model.Conditions
import kotlinx.coroutines.flow.Flow

class ObserveConditionsUseCase(private val repository: WeatherRepository) {
    fun execute(): Flow<Conditions?> = repository.observe()
}
