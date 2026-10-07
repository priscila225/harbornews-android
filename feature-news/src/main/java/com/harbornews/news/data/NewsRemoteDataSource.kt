package com.harbornews.news.data

import com.harbornews.common.StationConfig
import com.harbornews.news.data.dto.ArticleDetailDto
import com.harbornews.news.data.dto.ArticleDto

class NewsRemoteDataSource(
    private val api: NewsApi,
    private val station: StationConfig,
) {
    suspend fun headlines(section: String?): List<ArticleDto> = api.headlines(station.stationId, section)

    suspend fun article(id: String): ArticleDetailDto = api.article(station.stationId, id)

    suspend fun related(id: String): List<ArticleDto> = api.related(station.stationId, id)
}
