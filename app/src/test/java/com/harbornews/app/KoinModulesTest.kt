package com.harbornews.app

import android.content.Context
import androidx.work.WorkerParameters
import com.harbornews.app.di.appModule
import com.harbornews.common.DispatcherProvider
import com.harbornews.common.StationConfig
import com.harbornews.common.di.commonModule
import com.harbornews.news.di.newsModule
import com.harbornews.weather.di.weatherModule
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify
import retrofit2.Retrofit

@OptIn(KoinExperimentalAPI::class)
@DisplayName("Koin module graph")
class KoinModulesTest {
    private val platformTypes = listOf(Context::class, WorkerParameters::class)

    @Test
    fun `common module is complete`() {
        commonModule.verify()
    }

    @Test
    fun `news module is complete`() {
        newsModule.verify(extraTypes = listOf(Retrofit::class, StationConfig::class, DispatcherProvider::class))
    }

    @Test
    fun `weather module is complete`() {
        weatherModule.verify(
            extraTypes = platformTypes + listOf(Retrofit::class, StationConfig::class, DispatcherProvider::class),
        )
    }

    @Test
    fun `app module is complete`() {
        appModule.verify()
    }
}
