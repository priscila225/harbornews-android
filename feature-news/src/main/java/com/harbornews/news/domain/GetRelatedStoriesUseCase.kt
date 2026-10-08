package com.harbornews.news.domain

import com.harbornews.common.Result
import com.harbornews.news.domain.model.Article

class GetRelatedStoriesUseCase(private val repository: NewsRepository) {
    suspend fun execute(id: String): Result<List<Article>> = repository.relatedStories(id)
}
