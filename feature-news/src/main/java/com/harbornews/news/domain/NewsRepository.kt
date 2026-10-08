package com.harbornews.news.domain

import com.harbornews.common.Result
import com.harbornews.news.domain.model.Article
import com.harbornews.news.domain.model.ArticleDetail

interface NewsRepository {
    suspend fun headlines(section: String? = null): Result<List<Article>>

    suspend fun articleDetail(id: String): Result<ArticleDetail>

    suspend fun relatedStories(id: String): Result<List<Article>>
}
