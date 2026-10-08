package com.harbornews.news.data

import com.harbornews.news.data.dto.ArticleDetailDto
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

    @GET("stations/{station}/articles/{id}")
    suspend fun article(
        @Path("station") station: String,
        @Path("id") id: String,
    ): ArticleDetailDto

    @GET("stations/{station}/articles/{id}/related")
    suspend fun related(
        @Path("station") station: String,
        @Path("id") id: String,
    ): List<ArticleDto>
}
