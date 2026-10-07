package com.harbornews.weather.presentation

import com.harbornews.common.Result
import com.harbornews.testutils.InstantTaskExecutorExtension
import com.harbornews.testutils.TestDispatcherProvider
import com.harbornews.weather.domain.GetCurrentConditionsUseCase
import com.harbornews.weather.domain.GetHourlyForecastUseCase
import com.harbornews.weather.domain.ObserveConditionsUseCase
import com.harbornews.weather.domain.model.Conditions
import com.harbornews.weather.domain.model.HourlyEntry
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("WeatherViewModel")
class WeatherViewModelTest {
    private val dispatchers = TestDispatcherProvider()
    private val getCurrentConditions = mockk<GetCurrentConditionsUseCase>()
    private val getHourlyForecast = mockk<GetHourlyForecastUseCase>()
    private val observeConditions = mockk<ObserveConditionsUseCase> { every { execute() } returns flowOf(null) }
    private val viewModel = WeatherViewModel(getCurrentConditions, getHourlyForecast, observeConditions, dispatchers)

    @Test
    fun `starts in loading`() {
        viewModel.state.value shouldBe WeatherViewState.Loading
    }

    @Test
    fun `shows conditions after a refresh`() = runTest(dispatchers.testDispatcher) {
        val conditions = Conditions("s1", 70, "Sunny", 1L)
        coEvery { getCurrentConditions.execute() } returns Result.Success(conditions)

        viewModel.refresh()
        advanceUntilIdle()

        viewModel.state.value shouldBe WeatherViewState.Success(conditions)
    }

    @Test
    fun `shows unavailable when the use case fails`() = runTest(dispatchers.testDispatcher) {
        coEvery { getCurrentConditions.execute() } returns Result.Failure(RuntimeException("x"))

        viewModel.refresh()
        advanceUntilIdle()

        viewModel.state.value shouldBe WeatherViewState.Unavailable
    }

    @Test
    fun `publishes the hourly forecast`() = runTest(dispatchers.testDispatcher) {
        val hours = listOf(HourlyEntry(9, 61), HourlyEntry(10, 64))
        coEvery { getHourlyForecast.execute() } returns Result.Success(hours)

        viewModel.loadHourly()
        advanceUntilIdle()

        viewModel.hourly.value shouldBe hours
    }
}
