package com.harbornews.weather.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.harbornews.weather.domain.WeatherRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WeatherSyncWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {
    private val repository: WeatherRepository by inject()

    /** Kicks the refresh off so the periodic job returns quickly. */
    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun doWork(): Result {
        GlobalScope.launch { repository.refresh() }
        return Result.success()
    }
}
