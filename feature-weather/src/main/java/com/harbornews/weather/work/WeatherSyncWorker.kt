package com.harbornews.weather.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.harbornews.weather.domain.WeatherRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import com.harbornews.common.Result as AppResult

class WeatherSyncWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {
    private val repository: WeatherRepository by inject()

    override suspend fun doWork(): Result = when (repository.refresh()) {
        is AppResult.Success -> Result.success()
        is AppResult.Failure -> Result.retry()
    }
}
