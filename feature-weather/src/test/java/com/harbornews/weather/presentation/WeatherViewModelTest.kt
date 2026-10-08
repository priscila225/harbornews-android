package com.harbornews.weather.presentation

import com.harbornews.common.Result
import com.harbornews.testutils.InstantTaskExecutorExtension
import com.harbornews.weather.domain.GetCurrentConditionsUseCase
import com.harbornews.weather.domain.model.Conditions
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("WeatherViewModel")
class WeatherViewModelTest {
    private val testDispatcher = StandardTestDispatcher()
    private val getCurrentConditions = mockk<GetCurrentConditionsUseCase>()
    private lateinit var viewModel: WeatherViewModel

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WeatherViewModel(getCurrentConditions)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `starts in loading`() {
        viewModel.state.value shouldBe WeatherViewState.Loading
    }

    @Test
    fun `shows conditions after a refresh`() = runTest(testDispatcher) {
        val conditions = Conditions("s1", 70, "Sunny", 1L)
        coEvery { getCurrentConditions.execute() } returns Result.Success(conditions)

        viewModel.refresh()
        advanceUntilIdle()

        viewModel.state.value shouldBe WeatherViewState.Success(conditions)
    }

    @Test
    fun `shows unavailable when the use case fails`() = runTest(testDispatcher) {
        coEvery { getCurrentConditions.execute() } returns Result.Failure(RuntimeException("x"))

        viewModel.refresh()
        advanceUntilIdle()

        viewModel.state.value shouldBe WeatherViewState.Unavailable
    }
}
