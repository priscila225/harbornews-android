package com.harbornews.news.data

import com.harbornews.news.data.dto.ArticleDetailDto
import com.harbornews.news.domain.model.ArticleDetail

class ArticleDetailMapper(private val htmlStripper: HtmlStripper) {
    fun toDomain(dto: ArticleDetailDto): ArticleDetail? {
        val id = dto.id?.takeIf { it.isNotBlank() } ?: return null
        val title = dto.title?.takeIf { it.isNotBlank() } ?: return null
        return ArticleDetail(
            id = id,
            title = title.trim(),
            bodyText = htmlStripper.strip(dto.bodyHtml.orEmpty()),
            heroImageUrl = dto.heroImage!!.url,
            shareUrl = dto.shareUrl,
        )
    }
}
