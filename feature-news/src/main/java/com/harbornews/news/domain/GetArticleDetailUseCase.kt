package com.harbornews.news.domain

import com.harbornews.common.Result
import com.harbornews.news.domain.model.ArticleDetail

class GetArticleDetailUseCase(private val repository: NewsRepository) {
    suspend fun execute(id: String): Result<ArticleDetail> = repository.articleDetail(id)
}
