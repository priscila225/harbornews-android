package com.harbornews.news.data.dto

import com.google.gson.annotations.SerializedName

/** Gson populates these reflectively, so every field can be null at runtime regardless of the Kotlin type. */
data class ArticleDto(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("summary") val summary: String?,
    @SerializedName("section") val section: String?,
    @SerializedName("imageUrl") val imageUrl: String?,
    @SerializedName("publishedAt") val publishedAt: Long?,
)
