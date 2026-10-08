package com.harbornews.news.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.harbornews.common.ads.AdsListener
import com.harbornews.common.ads.AdsManager
import com.harbornews.common.weather.WeatherStripFactory
import com.harbornews.news.databinding.ActivityNewsBinding
import com.harbornews.news.presentation.NewsEvent
import com.harbornews.news.presentation.NewsViewModel
import com.harbornews.news.presentation.NewsViewState
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class NewsActivity : AppCompatActivity(), AdsListener {
    private val viewModel: NewsViewModel by viewModel()
    private val adsManager: AdsManager by inject()
    private val weatherStripFactory: WeatherStripFactory by inject()
    private lateinit var binding: ActivityNewsBinding
    private val adapter = ArticleAdapter { article ->
        Timber.d("Article tapped: %s", article.id)
        startActivity(
            Intent(this, ArticleDetailActivity::class.java).putExtra(ArticleDetailActivity.EXTRA_ARTICLE_ID, article.id),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.articles.adapter = adapter
        binding.headerContainer.addView(weatherStripFactory.create(this))

        viewModel.state.observe(this, ::render)
        viewModel.events.observe(this) { event ->
            when (val content = event.consume()) {
                is NewsEvent.ShowMessage -> Toast.makeText(this, content.message, Toast.LENGTH_SHORT).show()
                null -> Unit
            }
        }
        viewModel.load()
    }

    override fun onStart() {
        super.onStart()
        adsManager.attach(this)
    }

    override fun onStop() {
        adsManager.detach()
        super.onStop()
    }

    override fun onAdLoaded() {
        Timber.d("Ad loaded")
    }

    private fun render(state: NewsViewState) {
        binding.progress.visibility = if (state is NewsViewState.Loading) View.VISIBLE else View.GONE
        if (state is NewsViewState.Success) adapter.submitList(state.articles)
    }
}
