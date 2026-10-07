package com.harbornews.news.di

import com.harbornews.news.data.ArticleMapper
import com.harbornews.news.data.NewsApi
import com.harbornews.news.data.NewsRemoteDataSource
import com.harbornews.news.data.NewsRepositoryImpl
import com.harbornews.news.domain.GetHeadlinesUseCase
import com.harbornews.news.domain.NewsRepository
import com.harbornews.news.presentation.NewsViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val newsModule = module {
    single<NewsApi> { get<Retrofit>().create(NewsApi::class.java) }
    factory { ArticleMapper() }
    factory { NewsRemoteDataSource(get(), get()) }
    single<NewsRepository> { NewsRepositoryImpl(get(), get(), get()) }
    factory { GetHeadlinesUseCase(get()) }
    viewModel { NewsViewModel(get(), get()) }
}
