package com.harbornews.news.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harbornews.common.ads.AdsListener
import com.harbornews.common.ads.AdsManager
import com.harbornews.news.databinding.ActivityArticleDetailBinding
import com.harbornews.news.presentation.ArticleDetailViewModel
import com.harbornews.news.presentation.ArticleDetailViewState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class ArticleDetailActivity : AppCompatActivity(), AdsListener {
    private val viewModel: ArticleDetailViewModel by viewModel()
    private val adsManager: AdsManager by inject()
    private val shareHelper: ShareHelper by inject()
    private val readLaterStore: ReadLaterStore by inject()
    private lateinit var binding: ActivityArticleDetailBinding
    private val relatedAdapter = ArticleAdapter { }
    private var shareUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val articleId = intent.getStringExtra(EXTRA_ARTICLE_ID) ?: return finish()

        binding.related.adapter = relatedAdapter
        binding.shareButton.setOnClickListener { shareUrl?.let(shareHelper::share) }
        binding.readLaterButton.setOnClickListener {
            val saved = readLaterStore.toggle(articleId)
            binding.readLaterButton.text = if (saved) "Saved" else "Read later"
        }
        binding.readLaterButton.text = if (readLaterStore.isSaved(articleId)) "Saved" else "Read later"

        adsManager.attach(this)
        viewModel.state.observe(this, ::render)
        viewModel.related.observe(this) { relatedAdapter.submitList(it) }
        viewModel.load(articleId)
    }

    override fun onAdLoaded() {
        binding.adSlot.text = "Ad loaded"
    }

    private fun render(state: ArticleDetailViewState) {
        when (state) {
            ArticleDetailViewState.Loading -> binding.title.text = "Loading..."
            is ArticleDetailViewState.Error -> binding.title.text = state.message
            is ArticleDetailViewState.Success -> {
                binding.title.text = state.article.title
                binding.body.text = state.article.bodyText
                shareUrl = state.article.shareUrl
            }
        }
    }

    companion object {
        const val EXTRA_ARTICLE_ID = "article_id"
    }
}
