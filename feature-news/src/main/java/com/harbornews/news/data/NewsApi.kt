package com.harbornews.news.data

import com.harbornews.news.data.dto.ArticleDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NewsApi {
    @GET("stations/{station}/headlines")
    suspend fun headlines(
        @Path("station") station: String,
        @Query("section") section: String?,
    ): List<ArticleDto>
}
