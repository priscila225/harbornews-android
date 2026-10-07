package com.harbornews.news.ui

import android.content.Context
import android.widget.FrameLayout
import androidx.test.core.app.ApplicationProvider
import com.harbornews.news.domain.model.Article
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ArticleAdapterTest {
    @Test
    fun `binds title and summary and reports clicks`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        var clicked: Article? = null
        val adapter = ArticleAdapter { clicked = it }
        val article = Article("a1", "Harbor reopens", "Ferries are running", "local", null, 0L)
        adapter.submitList(listOf(article))

        val parent = FrameLayout(context)
        val holder = adapter.onCreateViewHolder(parent, 0)
        adapter.onBindViewHolder(holder, 0)
        holder.binding.root.performClick()

        assertEquals("Harbor reopens", holder.binding.title.text.toString())
        assertEquals("Ferries are running", holder.binding.summary.text.toString())
        assertEquals(article, clicked)
    }
}
