package com.harbornews.weather.data

import com.harbornews.common.Result
import com.harbornews.common.StationConfig
import com.harbornews.testutils.TestDispatcherProvider
import com.harbornews.weather.data.db.ConditionsDao
import com.harbornews.weather.data.dto.ConditionsDto
import com.harbornews.weather.data.dto.HourlyDto
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("WeatherRepositoryImpl")
class WeatherRepositoryImplTest {
    private val dispatchers = TestDispatcherProvider()
    private val api = mockk<WeatherApi>()
    private val dao = mockk<ConditionsDao>(relaxed = true)
    private val repository = WeatherRepositoryImpl(api, dao, ConditionsMapper(), StationConfig("s1"), dispatchers)

    @Test
    fun `refresh stores the new reading`() = runTest(dispatchers.testDispatcher) {
        coEvery { api.current("s1") } returns ConditionsDto(70.2, "Sunny", 5L)

        val result = repository.refresh()

        result.shouldBeInstanceOf<Result.Success<*>>()
        coVerify(exactly = 1) { dao.upsert(any()) }
    }

    @Test
    fun `refresh fails when the reading has no temperature`() = runTest(dispatchers.testDispatcher) {
        coEvery { api.current("s1") } returns ConditionsDto(null, "Sunny", 5L)

        repository.refresh().shouldBeInstanceOf<Result.Failure>()
        coVerify(exactly = 0) { dao.upsert(any()) }
    }

    @Test
    fun `hourly keeps the first six valid entries`() = runTest(dispatchers.testDispatcher) {
        val rows = (0..9).map { HourlyDto(it, 60.0 + it) } + HourlyDto(null, 1.0)
        coEvery { api.hourly("s1") } returns rows

        val entries = (repository.hourly() as Result.Success).value

        entries.map { it.hour } shouldBe listOf(0, 1, 2, 3, 4, 5)
    }
}
