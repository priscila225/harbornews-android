package com.harbornews.news.domain

import com.harbornews.common.Result
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("GetHeadlinesUseCase")
class GetHeadlinesUseCaseTest {
    private val repository = mockk<NewsRepository>()
    private val useCase = GetHeadlinesUseCase(repository)

    @Test
    fun `delegates to the repository with the requested section`() = runTest {
        coEvery { repository.headlines("sports") } returns Result.Success(emptyList())

        useCase.execute("sports") shouldBe Result.Success(emptyList())

        coVerify(exactly = 1) { repository.headlines("sports") }
    }
}
