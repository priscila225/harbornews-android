package com.harbornews.news.data

import com.harbornews.common.StationConfig
import com.harbornews.news.data.dto.ArticleDto

class NewsRemoteDataSource(
    private val api: NewsApi,
    private val station: StationConfig,
) {
    suspend fun headlines(section: String?): List<ArticleDto> = api.headlines(station.stationId, section)
}
