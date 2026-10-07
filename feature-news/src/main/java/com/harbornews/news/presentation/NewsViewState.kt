package com.harbornews.news.presentation

import com.harbornews.news.domain.model.Article

sealed class NewsViewState {
    data object Loading : NewsViewState()

    data class Success(val articles: List<Article>) : NewsViewState()

    data class Error(val message: String) : NewsViewState()
}

sealed class NewsEvent {
    data class ShowMessage(val message: String) : NewsEvent()
}
