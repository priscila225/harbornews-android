package com.harbornews.news.domain.model

data class Article(
    val id: String,
    val title: String,
    val summary: String,
    val section: String,
    val imageUrl: String?,
    val publishedAtMillis: Long,
)
