package com.harbornews.news.presentation

import com.harbornews.common.Result
import com.harbornews.news.domain.GetArticleDetailUseCase
import com.harbornews.news.domain.GetRelatedStoriesUseCase
import com.harbornews.news.domain.model.Article
import com.harbornews.news.domain.model.ArticleDetail
import com.harbornews.testutils.InstantTaskExecutorExtension
import com.harbornews.testutils.TestDispatcherProvider
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("ArticleDetailViewModel")
class ArticleDetailViewModelTest {
    private val dispatchers = TestDispatcherProvider(bg = UnconfinedTestDispatcher())
    private val getArticleDetail = mockk<GetArticleDetailUseCase>()
    private val getRelatedStories = mockk<GetRelatedStoriesUseCase>()
    private val viewModel = ArticleDetailViewModel(getArticleDetail, getRelatedStories, dispatchers)
    private val detail = ArticleDetail("a1", "Harbor reopens", "Body", null, "https://harbornews.example/a1")
    private val related = Article("r1", "Related", "S", "local", null, 0L)

    @Test
    fun `shows the article once it loads`() = runTest(dispatchers.testDispatcher) {
        coEvery { getArticleDetail.execute("a1") } returns Result.Success(detail)
        coEvery { getRelatedStories.execute("a1") } returns Result.Success(listOf(related))

        viewModel.load("a1")
        runCurrent()

        viewModel.state.value shouldBe ArticleDetailViewState.Success(detail)
        viewModel.related.value shouldBe listOf(related)
    }

    @Test
    fun `shows an error when the article cannot be loaded`() = runTest(dispatchers.testDispatcher) {
        coEvery { getArticleDetail.execute("a1") } returns Result.Failure(IllegalStateException("gone"))
        coEvery { getRelatedStories.execute("a1") } returns Result.Success(emptyList())

        viewModel.load("a1")
        runCurrent()

        viewModel.state.value.shouldBeInstanceOf<ArticleDetailViewState.Error>()
    }
}
