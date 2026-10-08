package com.harbornews.weather.data.dto

import com.google.gson.annotations.SerializedName

data class HourlyDto(
    @SerializedName("hour") val hour: Int?,
    @SerializedName("tempF") val tempF: Double?,
)
