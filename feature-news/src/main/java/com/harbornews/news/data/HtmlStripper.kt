package com.harbornews.news.data

class HtmlStripper {
    private val tags = Regex("<[^>]*>")
    private val whitespace = Regex("\\s+")

    fun strip(html: String): String = html.replace(tags, " ").replace(whitespace, " ").trim()
}
