package com.harbornews.news.data

import com.harbornews.news.data.dto.ArticleDto
import com.harbornews.news.domain.model.Article

class ArticleMapper {
    /** Returns null for rows that cannot be shown (no id or no title). */
    fun toDomain(dto: ArticleDto): Article? {
        val id = dto.id?.takeIf { it.isNotBlank() } ?: return null
        val title = dto.title?.takeIf { it.isNotBlank() } ?: return null
        return Article(
            id = id,
            title = title.trim(),
            summary = dto.summary.orEmpty().trim(),
            section = dto.section ?: DEFAULT_SECTION,
            imageUrl = dto.imageUrl,
            publishedAtMillis = dto.publishedAt ?: 0L,
        )
    }

    fun toDomain(dtos: List<ArticleDto>): List<Article> = dtos.mapNotNull(::toDomain)

    private companion object {
        const val DEFAULT_SECTION = "top"
    }
}
