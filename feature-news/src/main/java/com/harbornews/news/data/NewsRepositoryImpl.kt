package com.harbornews.news.data

import com.harbornews.common.DispatcherProvider
import com.harbornews.common.Result
import com.harbornews.news.domain.NewsRepository
import com.harbornews.news.domain.model.Article
import kotlinx.coroutines.withContext

class NewsRepositoryImpl(
    private val remote: NewsRemoteDataSource,
    private val mapper: ArticleMapper,
    private val dispatchers: DispatcherProvider,
) : NewsRepository {
    override suspend fun headlines(section: String?): Result<List<Article>> = withContext(dispatchers.bg) {
        Result.runCatching { mapper.toDomain(remote.headlines(section)) }
    }
}
