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
import io.mockk.coVerify
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
        coEvery { getHeadlines.execute(null) } returns Result.Success(listOf(article))

        val states = viewModel.state.captureValues {
            viewModel.load()
            advanceUntilIdle()
        }

        states.first() shouldBe NewsViewState.Loading
        states.last() shouldBe NewsViewState.Success(listOf(article))
    }

    @Test
    fun `emits error state and a one-shot message on failure`() = runTest(dispatchers.testDispatcher) {
        coEvery { getHeadlines.execute(null) } returns Result.Failure(IllegalStateException("boom"))

        val events = viewModel.events.captureValues {
            viewModel.load()
            advanceUntilIdle()
        }

        viewModel.state.value.shouldBeInstanceOf<NewsViewState.Error>()
        events.single().consume() shouldBe NewsEvent.ShowMessage("Headlines unavailable")
    }

    @Test
    fun `selecting a section reloads with that section`() = runTest(dispatchers.testDispatcher) {
        coEvery { getHeadlines.execute("local") } returns Result.Success(listOf(article))

        viewModel.selectSection("local")
        advanceUntilIdle()

        viewModel.section.value shouldBe "local"
        viewModel.state.value shouldBe NewsViewState.Success(listOf(article))
        coVerify(exactly = 1) { getHeadlines.execute("local") }
    }

    @Test
    fun `selecting the current section does not reload`() = runTest(dispatchers.testDispatcher) {
        coEvery { getHeadlines.execute(null) } returns Result.Success(emptyList())

        viewModel.selectSection(null)
        advanceUntilIdle()

        coVerify(exactly = 0) { getHeadlines.execute(any()) }
    }

    @Test
    fun `a newer selection replaces a load that is still running`() = runTest(dispatchers.testDispatcher) {
        coEvery { getHeadlines.execute("local") } returns Result.Success(listOf(article))
        coEvery { getHeadlines.execute("sports") } returns Result.Success(emptyList())

        viewModel.selectSection("local")
        viewModel.selectSection("sports")
        advanceUntilIdle()

        viewModel.state.value shouldBe NewsViewState.Success(emptyList())
        coVerify(exactly = 0) { getHeadlines.execute("local") }
    }
}
