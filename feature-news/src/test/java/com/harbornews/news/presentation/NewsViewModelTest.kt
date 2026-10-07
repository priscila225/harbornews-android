package com.harbornews.news.presentation

import com.harbornews.common.Result
import com.harbornews.news.domain.GetHeadlinesUseCase
import com.harbornews.news.domain.model.Article
import com.harbornews.testutils.InstantTaskExecutorExtension
import com.harbornews.testutils.TestDispatcherProvider
import com.harbornews.testutils.captureValues
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorExtension::class)
@DisplayName("NewsViewModel")
class NewsViewModelTest {
    private val dispatchers = TestDispatcherProvider()
    private val getHeadlines = mockk<GetHeadlinesUseCase>()
    private val viewModel = NewsViewModel(getHeadlines, dispatchers)
    private val article = Article("a1", "Title", "Summary", "top", null, 0L)

    @Test
    fun `emits loading then success`() = runTest(dispatchers.testDispatcher) {
        coEvery { getHeadlines.execute() } returns Result.Success(listOf(article))

        val states = viewModel.state.captureValues {
            viewModel.load()
            advanceUntilIdle()
        }

        states.first() shouldBe NewsViewState.Loading
        states.last() shouldBe NewsViewState.Success(listOf(article))
    }

    @Test
    fun `emits error state and a one-shot message on failure`() = runTest(dispatchers.testDispatcher) {
        coEvery { getHeadlines.execute() } returns Result.Failure(IllegalStateException("boom"))

        val events = viewModel.events.captureValues {
            viewModel.load()
            advanceUntilIdle()
        }

        viewModel.state.value.shouldBeInstanceOf<NewsViewState.Error>()
        events.single().consume() shouldBe NewsEvent.ShowMessage("Headlines unavailable")
    }
}
