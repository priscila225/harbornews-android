package com.harbornews.weather.data.dto

import com.google.gson.annotations.SerializedName

data class ConditionsDto(
    @SerializedName("tempF") val tempF: Double?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("updatedAt") val updatedAt: Long?,
)
