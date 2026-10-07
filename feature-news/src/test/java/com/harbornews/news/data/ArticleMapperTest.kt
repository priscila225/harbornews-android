package com.harbornews.news.data

import com.harbornews.news.data.dto.ArticleDto
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ArticleMapper")
class ArticleMapperTest {
    private val mapper = ArticleMapper()

    private fun dto(
        id: String? = "a1",
        title: String? = "  Harbor reopens  ",
        summary: String? = " Ferries are running ",
        section: String? = "local",
        publishedAt: Long? = 1_700_000_000_000,
    ) = ArticleDto(id, title, summary, section, imageUrl = null, publishedAt = publishedAt)

    @Test
    fun `maps a complete dto and trims text`() {
        val article = mapper.toDomain(dto())!!

        article.id shouldBe "a1"
        article.title shouldBe "Harbor reopens"
        article.summary shouldBe "Ferries are running"
        article.section shouldBe "local"
    }

    @Test
    fun `drops rows without id or title`() {
        mapper.toDomain(dto(id = null)).shouldBeNull()
        mapper.toDomain(dto(id = " ")).shouldBeNull()
        mapper.toDomain(dto(title = null)).shouldBeNull()
    }

    @Test
    fun `falls back to defaults for missing optional fields`() {
        val article = mapper.toDomain(dto(summary = null, section = null, publishedAt = null))!!

        article.summary shouldBe ""
        article.section shouldBe "top"
        article.publishedAtMillis shouldBe 0L
    }

    @Test
    fun `list mapping skips invalid rows`() {
        mapper.toDomain(listOf(dto(), dto(id = null), dto(id = "a2"))) shouldHaveSize 2
    }
}
