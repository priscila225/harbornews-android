package com.harbornews.weather.ui

import android.content.Context
import android.view.View
import androidx.compose.ui.platform.ComposeView
import com.harbornews.common.weather.WeatherStripFactory

class ComposeWeatherStripFactory : WeatherStripFactory {
    override fun create(context: Context): View = ComposeView(context).apply {
        setContent { WeatherWidget() }
    }
}
