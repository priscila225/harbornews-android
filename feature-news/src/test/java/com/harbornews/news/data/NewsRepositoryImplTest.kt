package com.harbornews.news.data

import com.harbornews.common.Result
import com.harbornews.news.data.dto.ArticleDetailDto
import com.harbornews.news.data.dto.ArticleDto
import com.harbornews.news.data.dto.ImageDto
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
    private val repository =
        NewsRepositoryImpl(remote, ArticleMapper(), ArticleDetailMapper(HtmlStripper()), HtmlStripper(), dispatchers)

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

    @Test
    fun `maps the article detail and strips markup from the body`() = runTest(dispatchers.testDispatcher) {
        val dto =
            ArticleDetailDto(
                "a1",
                "Harbor reopens",
                "<p>Ferries <b>run</b></p>",
                ImageDto("https://img.example/1.jpg", null),
                null
            )
        coEvery { remote.article("a1") } returns dto

        val result = repository.articleDetail("a1")

        result.shouldBeInstanceOf<Result.Success<*>>()
        (result as Result.Success).value.bodyText shouldBe "Ferries run"
    }

    @Test
    fun `related stories are de-duplicated and capped`() = runTest(dispatchers.testDispatcher) {
        val rows = (1..8).map { ArticleDto("r${it % 6}", "Title $it", "<i>s</i>", null, null, null) }
        coEvery { remote.related("a1") } returns rows

        val result = repository.relatedStories("a1") as Result.Success

        result.value.map { it.id }.distinct().size shouldBe result.value.size
        (result.value.size <= 5) shouldBe true
        result.value.first().summary shouldBe "s"
    }
}
