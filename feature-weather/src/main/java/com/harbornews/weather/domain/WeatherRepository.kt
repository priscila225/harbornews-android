package com.harbornews.weather.domain

import com.harbornews.common.Result
import com.harbornews.weather.domain.model.Conditions
import com.harbornews.weather.domain.model.HourlyEntry
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    /** Last stored reading, if any. */
    suspend fun cached(): Conditions?

    /** Fetches a fresh reading and stores it. */
    suspend fun refresh(): Result<Conditions>

    /** Emits the stored reading whenever it changes. */
    fun observe(): Flow<Conditions?>

    suspend fun hourly(): Result<List<HourlyEntry>>
}
