package com.harbornews.weather.data

import com.harbornews.weather.data.dto.ConditionsDto
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApi {
    @GET("stations/{station}/weather/current")
    suspend fun current(
        @Path("station") station: String
    ): ConditionsDto
}
