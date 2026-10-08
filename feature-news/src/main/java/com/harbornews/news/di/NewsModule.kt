package com.harbornews.news.di

import com.harbornews.news.data.ArticleDetailMapper
import com.harbornews.news.data.ArticleMapper
import com.harbornews.news.data.HtmlStripper
import com.harbornews.news.data.NewsApi
import com.harbornews.news.data.NewsRemoteDataSource
import com.harbornews.news.data.NewsRepositoryImpl
import com.harbornews.news.domain.GetArticleDetailUseCase
import com.harbornews.news.domain.GetHeadlinesUseCase
import com.harbornews.news.domain.GetRelatedStoriesUseCase
import com.harbornews.news.domain.NewsRepository
import com.harbornews.news.presentation.ArticleDetailViewModel
import com.harbornews.news.presentation.NewsViewModel
import com.harbornews.news.ui.ReadLaterStore
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val newsModule = module {
    single<NewsApi> { get<Retrofit>().create(NewsApi::class.java) }
    factory { ArticleMapper() }
    factory { HtmlStripper() }
    factory { ArticleDetailMapper(get()) }
    factory { NewsRemoteDataSource(get(), get()) }
    single<NewsRepository> { NewsRepositoryImpl(get(), get(), get(), get(), get()) }
    factory { GetHeadlinesUseCase(get()) }
    factory { GetArticleDetailUseCase(get()) }
    factory { GetRelatedStoriesUseCase(get()) }
    factory { ReadLaterStore(androidContext()) }
    viewModel { NewsViewModel(get(), get()) }
    single { ArticleDetailViewModel(get(), get(), get()) }
}
