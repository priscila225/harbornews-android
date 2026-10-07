package com.harbornews.news.data

import com.harbornews.common.DispatcherProvider
import com.harbornews.common.Result
import com.harbornews.news.domain.NewsRepository
import com.harbornews.news.domain.model.Article
import com.harbornews.news.domain.model.ArticleDetail
import kotlinx.coroutines.withContext

class NewsRepositoryImpl(
    private val remote: NewsRemoteDataSource,
    private val mapper: ArticleMapper,
    private val detailMapper: ArticleDetailMapper,
    private val htmlStripper: HtmlStripper,
    private val dispatchers: DispatcherProvider,
) : NewsRepository {
    override suspend fun headlines(section: String?): Result<List<Article>> = withContext(dispatchers.bg) {
        Result.runCatching { mapper.toDomain(remote.headlines(section)) }
    }

    override suspend fun articleDetail(id: String): Result<ArticleDetail> = withContext(dispatchers.bg) {
        Result.runCatching {
            detailMapper.toDomain(remote.article(id)) ?: error("Article $id is unavailable")
        }
    }

    override suspend fun relatedStories(id: String): Result<List<Article>> = withContext(dispatchers.ui) {
        Result.runCatching {
            mapper.toDomain(remote.related(id))
                .map { it.copy(summary = htmlStripper.strip(it.summary)) }
                .distinctBy { it.id }
                .take(MAX_RELATED)
        }
    }

    private companion object {
        const val MAX_RELATED = 5
    }
}
