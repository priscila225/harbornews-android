package com.harbornews.news.domain

import com.harbornews.common.Result
import com.harbornews.news.domain.model.Article

interface NewsRepository {
    suspend fun headlines(section: String? = null): Result<List<Article>>
}
