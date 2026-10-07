package com.harbornews.weather.di

import androidx.room.Room
import com.harbornews.common.weather.WeatherStripFactory
import com.harbornews.weather.data.ConditionsMapper
import com.harbornews.weather.data.WeatherApi
import com.harbornews.weather.data.WeatherRepositoryImpl
import com.harbornews.weather.data.db.ConditionsDao
import com.harbornews.weather.data.db.WeatherDatabase
import com.harbornews.weather.domain.GetCurrentConditionsUseCase
import com.harbornews.weather.domain.WeatherRepository
import com.harbornews.weather.presentation.WeatherViewModel
import com.harbornews.weather.ui.ComposeWeatherStripFactory
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit

val weatherModule = module {
    single<WeatherApi> { get<Retrofit>().create(WeatherApi::class.java) }
    single<WeatherDatabase> { Room.databaseBuilder(androidContext(), WeatherDatabase::class.java, "weather.db").build() }
    single<ConditionsDao> { get<WeatherDatabase>().conditionsDao() }
    factory { ConditionsMapper() }
    single<WeatherRepository> { WeatherRepositoryImpl(get(), get(), get(), get(), get()) }
    factory { GetCurrentConditionsUseCase(get()) }
    viewModel { WeatherViewModel(get(), get()) }
    factory<WeatherStripFactory> { ComposeWeatherStripFactory() }
}
