package com.harbornews.weather.domain

import com.harbornews.common.Result
import com.harbornews.weather.domain.model.Conditions

interface WeatherRepository {
    /** Last stored reading, if any. */
    suspend fun cached(): Conditions?

    /** Fetches a fresh reading and stores it. */
    suspend fun refresh(): Result<Conditions>
}
