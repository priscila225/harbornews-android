package com.harbornews.weather.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harbornews.common.BaseViewModel
import com.harbornews.common.DispatcherProvider
import com.harbornews.weather.domain.GetCurrentConditionsUseCase
import com.harbornews.weather.domain.GetHourlyForecastUseCase
import com.harbornews.weather.domain.ObserveConditionsUseCase
import com.harbornews.weather.domain.model.Conditions
import com.harbornews.weather.domain.model.HourlyEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

sealed class WeatherNavEvent {
    data object OpenAlerts : WeatherNavEvent()
}

class WeatherViewModel(
    private val getCurrentConditions: GetCurrentConditionsUseCase,
    private val getHourlyForecast: GetHourlyForecastUseCase,
    observeConditions: ObserveConditionsUseCase,
    dispatchers: DispatcherProvider,
) : BaseViewModel(dispatchers) {
    private val _state = MutableLiveData<WeatherViewState>(WeatherViewState.Loading)
    val state: LiveData<WeatherViewState> = _state

    private val _hourly = MutableLiveData<List<HourlyEntry>>(emptyList())
    val hourly: LiveData<List<HourlyEntry>> = _hourly

    /** Live view of the stored reading, updated by background syncs. */
    val conditions: Flow<Conditions?> = observeConditions.execute()

    private val _navigation = MutableSharedFlow<WeatherNavEvent>()
    val navigation: Flow<WeatherNavEvent> = _navigation

    fun refresh() {
        launch(dispatchers.ui) {
            getCurrentConditions.execute().fold(
                onSuccess = { _state.value = WeatherViewState.Success(it) },
                onFailure = { _state.value = WeatherViewState.Unavailable },
            )
        }
    }

    fun loadHourly() {
        launch(dispatchers.ui) {
            getHourlyForecast.execute().fold(
                onSuccess = { _hourly.value = it },
                onFailure = { },
            )
        }
    }

    fun onAlertsClicked() {
        _navigation.tryEmit(WeatherNavEvent.OpenAlerts)
    }
}
