package com.harbornews.weather.data

import com.harbornews.common.DispatcherProvider
import com.harbornews.common.Result
import com.harbornews.common.StationConfig
import com.harbornews.weather.data.db.ConditionsDao
import com.harbornews.weather.domain.WeatherRepository
import com.harbornews.weather.domain.model.Conditions
import kotlinx.coroutines.withContext

class WeatherRepositoryImpl(
    private val api: WeatherApi,
    private val dao: ConditionsDao,
    private val mapper: ConditionsMapper,
    private val station: StationConfig,
    private val dispatchers: DispatcherProvider,
) : WeatherRepository {
    override suspend fun cached(): Conditions? = withContext(dispatchers.bg) {
        dao.get(station.stationId)?.let(mapper::toDomain)
    }

    override suspend fun refresh(): Result<Conditions> = withContext(dispatchers.bg) {
        Result.runCatching {
            val dto = api.current(station.stationId)
            val conditions = checkNotNull(mapper.toDomain(station.stationId, dto)) { "No temperature in response" }
            dao.upsert(mapper.toEntity(conditions))
            conditions
        }
    }
}
