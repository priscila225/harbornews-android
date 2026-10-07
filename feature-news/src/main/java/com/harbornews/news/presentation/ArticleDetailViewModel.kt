package com.harbornews.news.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harbornews.common.BaseViewModel
import com.harbornews.common.DispatcherProvider
import com.harbornews.news.domain.GetArticleDetailUseCase
import com.harbornews.news.domain.GetRelatedStoriesUseCase
import com.harbornews.news.domain.model.Article
import com.harbornews.news.domain.model.ArticleDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

sealed class ArticleDetailViewState {
    data object Loading : ArticleDetailViewState()

    data class Success(val article: ArticleDetail) : ArticleDetailViewState()

    data class Error(val message: String) : ArticleDetailViewState()
}

class ArticleDetailViewModel(
    private val getArticleDetail: GetArticleDetailUseCase,
    private val getRelatedStories: GetRelatedStoriesUseCase,
    dispatchers: DispatcherProvider,
) : BaseViewModel(dispatchers) {
    private val _state = MutableLiveData<ArticleDetailViewState>(ArticleDetailViewState.Loading)
    val state: LiveData<ArticleDetailViewState> = _state

    private val _related = MutableLiveData<List<Article>>(emptyList())
    val related: LiveData<List<Article>> = _related

    fun load(articleId: String) {
        launch(dispatchers.ui) {
            getArticleDetail.execute(articleId).fold(
                onSuccess = { _state.value = ArticleDetailViewState.Success(it) },
                onFailure = { _state.value = ArticleDetailViewState.Error(it.message ?: "Article unavailable") },
            )
        }
        startRelatedPolling(articleId)
    }

    /** Keeps the related list fresh while the screen is open. */
    private fun startRelatedPolling(articleId: String) {
        CoroutineScope(dispatchers.bg).launch {
            while (isActive) {
                getRelatedStories.execute(articleId).fold(
                    onSuccess = { _related.postValue(it) },
                    onFailure = { },
                )
                delay(POLL_INTERVAL_MS)
            }
        }
    }

    private companion object {
        const val POLL_INTERVAL_MS = 30_000L
    }
}
