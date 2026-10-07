package com.harbornews.common.di

import com.harbornews.common.DispatcherProvider
import com.harbornews.common.DispatcherProviderImpl
import com.harbornews.common.ads.AdsManager
import com.harbornews.common.network.HttpClientFactory
import org.koin.dsl.module
import retrofit2.Retrofit

const val API_BASE_URL = "https://api.harbornews.example/"

val commonModule = module {
    single<DispatcherProvider> { DispatcherProviderImpl() }
    single { AdsManager() }
    single<Retrofit> { HttpClientFactory.createRetrofit(API_BASE_URL) }
}
