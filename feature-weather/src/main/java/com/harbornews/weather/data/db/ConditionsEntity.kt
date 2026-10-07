package com.harbornews.weather.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "conditions")
data class ConditionsEntity(
    @PrimaryKey val stationId: String,
    val temperatureF: Int,
    val summary: String,
    val updatedAtMillis: Long,
)
