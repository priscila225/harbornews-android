package com.harbornews.app.di

import com.harbornews.common.StationConfig
import org.koin.dsl.module

const val DEFAULT_STATION_ID = "harbor-1"

val appModule = module {
    single { StationConfig(DEFAULT_STATION_ID) }
}
