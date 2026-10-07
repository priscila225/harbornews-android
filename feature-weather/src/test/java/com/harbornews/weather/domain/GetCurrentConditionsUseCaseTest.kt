package com.harbornews.weather.domain

import com.harbornews.common.Result
import com.harbornews.weather.domain.model.Conditions
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GetCurrentConditionsUseCase")
class GetCurrentConditionsUseCaseTest {
    private val repository = mockk<WeatherRepository>()
    private val useCase = GetCurrentConditionsUseCase(repository)
    private val fresh = Conditions("s1", 70, "Sunny", 2L)
    private val stale = Conditions("s1", 60, "Cloudy", 1L)

    @Test
    fun `prefers the fresh reading`() = runTest {
        coEvery { repository.refresh() } returns Result.Success(fresh)

        useCase.execute() shouldBe Result.Success(fresh)
    }

    @Test
    fun `falls back to the stored reading when refresh fails`() = runTest {
        coEvery { repository.refresh() } returns Result.Failure(java.io.IOException("offline"))
        coEvery { repository.cached() } returns stale

        useCase.execute() shouldBe Result.Success(stale)
    }

    @Test
    fun `reports the failure when nothing is stored`() = runTest {
        coEvery { repository.refresh() } returns Result.Failure(java.io.IOException("offline"))
        coEvery { repository.cached() } returns null

        useCase.execute().shouldBeInstanceOf<Result.Failure>()
    }
}
