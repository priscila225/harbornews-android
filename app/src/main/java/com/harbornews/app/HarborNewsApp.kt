package com.harbornews.app

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.harbornews.app.di.appModule
import com.harbornews.common.di.commonModule
import com.harbornews.news.di.newsModule
import com.harbornews.weather.di.weatherModule
import com.harbornews.weather.work.WeatherSyncWorker
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber
import java.util.concurrent.TimeUnit

class HarborNewsApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        startKoin {
            androidContext(this@HarborNewsApp)
            modules(commonModule, appModule, newsModule, weatherModule)
        }
        scheduleWeatherSync()
    }

    private fun scheduleWeatherSync() {
        val request = PeriodicWorkRequestBuilder<WeatherSyncWorker>(SYNC_INTERVAL_MINUTES, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork("weather-sync", ExistingPeriodicWorkPolicy.KEEP, request)
    }

    private companion object {
        const val SYNC_INTERVAL_MINUTES = 30L
    }
}
