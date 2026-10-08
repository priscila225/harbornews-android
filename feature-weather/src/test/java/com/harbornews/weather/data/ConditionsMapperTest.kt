package com.harbornews.weather.data

import com.harbornews.weather.data.dto.ConditionsDto
import com.harbornews.weather.data.dto.HourlyDto
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ConditionsMapper")
class ConditionsMapperTest {
    private val mapper = ConditionsMapper()

    @Test
    fun `rounds the temperature and keeps the summary`() {
        val conditions = mapper.toDomain("s1", ConditionsDto(71.6, "Sunny", 10L))!!

        conditions.temperatureF shouldBe 72
        conditions.summary shouldBe "Sunny"
        conditions.updatedAtMillis shouldBe 10L
    }

    @Test
    fun `a reading without a temperature is dropped`() {
        mapper.toDomain("s1", ConditionsDto(null, "Sunny", 10L)).shouldBeNull()
    }

    @Test
    fun `entity round trip keeps every field`() {
        val conditions = mapper.toDomain("s1", ConditionsDto(50.0, "Fog", 5L))!!

        mapper.toDomain(mapper.toEntity(conditions)) shouldBe conditions
    }

    @Test
    fun `hourly rows need both an hour and a temperature`() {
        mapper.toHourly(HourlyDto(9, 61.4))?.temperatureF shouldBe 61
        mapper.toHourly(HourlyDto(null, 61.4)).shouldBeNull()
        mapper.toHourly(HourlyDto(9, null)).shouldBeNull()
    }
}
