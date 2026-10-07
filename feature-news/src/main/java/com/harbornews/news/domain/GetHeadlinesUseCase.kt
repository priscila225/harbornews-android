package com.harbornews.news.domain

import com.harbornews.common.Result
import com.harbornews.news.domain.model.Article

class GetHeadlinesUseCase(private val repository: NewsRepository) {
    suspend fun execute(section: String? = null): Result<List<Article>> = repository.headlines(section)
}
