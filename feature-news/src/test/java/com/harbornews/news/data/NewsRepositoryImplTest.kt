package com.harbornews.news.data

import com.harbornews.common.Result
import com.harbornews.news.data.dto.ArticleDto
import com.harbornews.testutils.TestDispatcherProvider
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("NewsRepositoryImpl")
class NewsRepositoryImplTest {
    private val remote = mockk<NewsRemoteDataSource>()
    private val dispatchers = TestDispatcherProvider()
    private val repository = NewsRepositoryImpl(remote, ArticleMapper(), dispatchers)

    @Test
    fun `returns mapped articles on success`() = runTest(dispatchers.testDispatcher) {
        coEvery { remote.headlines(null) } returns listOf(ArticleDto("a1", "Title", null, null, null, null))

        val result = repository.headlines()

        result.shouldBeInstanceOf<Result.Success<*>>()
        (result as Result.Success).value.map { it.id } shouldBe listOf("a1")
    }

    @Test
    fun `wraps remote failures`() = runTest(dispatchers.testDispatcher) {
        coEvery { remote.headlines("local") } throws java.io.IOException("offline")

        val result = repository.headlines("local")

        result.shouldBeInstanceOf<Result.Failure>()
        (result as Result.Failure).error.message shouldBe "offline"
    }
}
