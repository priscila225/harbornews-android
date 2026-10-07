package com.harbornews.news.data

import com.harbornews.news.data.dto.ArticleDetailDto
import com.harbornews.news.data.dto.ImageDto
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

@DisplayName("ArticleDetailMapper")
class ArticleDetailMapperTest {
    private val mapper = ArticleDetailMapper(HtmlStripper())

    private fun dto(id: String? = "a1", title: String? = " Harbor reopens ") = ArticleDetailDto(
        id = id,
        title = title,
        bodyHtml = "<p>Ferries are <b>running</b>.</p>",
        heroImage = ImageDto("https://img.example/hero.jpg", "Pier"),
        shareUrl = "https://harbornews.example/a1",
    )

    @Test
    fun `maps a complete dto`() {
        val detail = mapper.toDomain(dto())!!

        detail.title shouldBe "Harbor reopens"
        detail.bodyText shouldBe "Ferries are running ."
        detail.heroImageUrl shouldBe "https://img.example/hero.jpg"
        detail.shareUrl shouldBe "https://harbornews.example/a1"
    }

    @Test
    fun `rows without id or title are dropped`() {
        mapper.toDomain(dto(id = null)).shouldBeNull()
        mapper.toDomain(dto(title = " ")).shouldBeNull()
    }
}
