package com.harbornews.news.data.dto

import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("url") val url: String?,
    @SerializedName("caption") val caption: String?,
)

data class ArticleDetailDto(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("bodyHtml") val bodyHtml: String?,
    @SerializedName("heroImage") val heroImage: ImageDto?,
    @SerializedName("shareUrl") val shareUrl: String?,
)
