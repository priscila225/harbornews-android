package com.harbornews.weather.domain

import com.harbornews.common.Result
import com.harbornews.weather.domain.model.Conditions

class GetCurrentConditionsUseCase(private val repository: WeatherRepository) {
    /** Fresh data when the network works, otherwise the last stored reading. */
    suspend fun execute(): Result<Conditions> {
        val fresh = repository.refresh()
        if (fresh is Result.Success) return fresh
        val cached = repository.cached() ?: return fresh
        return Result.Success(cached)
    }
}
