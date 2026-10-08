package com.harbornews.weather.data

import com.harbornews.weather.data.dto.ConditionsDto
import com.harbornews.weather.data.dto.HourlyDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApi {
    @GET("stations/{station}/weather/current")
    suspend fun current(
        @Path("station") station: String
    ): ConditionsDto

    @GET("stations/{station}/weather/hourly")
    suspend fun hourly(
        @Path("station") station: String
    ): List<HourlyDto>
}
