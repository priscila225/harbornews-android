package com.harbornews.news.domain.model

data class ArticleDetail(
    val id: String,
    val title: String,
    val bodyText: String,
    val heroImageUrl: String?,
    val shareUrl: String?,
)
