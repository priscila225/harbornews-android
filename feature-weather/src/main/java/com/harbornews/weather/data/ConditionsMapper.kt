package com.harbornews.weather.data

import com.harbornews.weather.data.db.ConditionsEntity
import com.harbornews.weather.data.dto.ConditionsDto
import com.harbornews.weather.domain.model.Conditions
import kotlin.math.roundToInt

class ConditionsMapper {
    /** A reading without a temperature is useless to show, so it maps to null. */
    fun toDomain(stationId: String, dto: ConditionsDto): Conditions? {
        val temp = dto.tempF ?: return null
        return Conditions(
            stationId = stationId,
            temperatureF = temp.roundToInt(),
            summary = dto.summary.orEmpty(),
            updatedAtMillis = dto.updatedAt ?: 0L,
        )
    }

    fun toEntity(conditions: Conditions) = ConditionsEntity(
        stationId = conditions.stationId,
        temperatureF = conditions.temperatureF,
        summary = conditions.summary,
        updatedAtMillis = conditions.updatedAtMillis,
    )

    fun toDomain(entity: ConditionsEntity) = Conditions(
        stationId = entity.stationId,
        temperatureF = entity.temperatureF,
        summary = entity.summary,
        updatedAtMillis = entity.updatedAtMillis,
    )
}
