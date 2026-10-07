package com.harbornews.common.weather

import android.content.Context
import android.view.View

/** Lets feature modules show the weather strip without depending on each other. */
interface WeatherStripFactory {
    fun create(context: Context): View
}
