package com.harbornews.weather.presentation

import com.harbornews.common.Result
import com.harbornews.testutils.InstantTaskExecutorExtension
import com.harbornews.testutils.TestDispatcherProvider
import com.harbornews.weather.domain.GetCurrentConditionsUseCase
import com.harbornews.weather.domain.model.Conditions
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
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
    private val viewModel = WeatherViewModel(getCurrentConditions, dispatchers)

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
}
