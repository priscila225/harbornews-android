package com.harbornews.weather.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harbornews.common.BaseViewModel
import com.harbornews.common.DispatcherProvider
import com.harbornews.weather.domain.GetCurrentConditionsUseCase
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getCurrentConditions: GetCurrentConditionsUseCase,
    dispatchers: DispatcherProvider,
) : BaseViewModel(dispatchers) {
    private val _state = MutableLiveData<WeatherViewState>(WeatherViewState.Loading)
    val state: LiveData<WeatherViewState> = _state

    fun refresh() {
        launch(dispatchers.ui) {
            getCurrentConditions.execute().fold(
                onSuccess = { _state.value = WeatherViewState.Success(it) },
                onFailure = { _state.value = WeatherViewState.Unavailable },
            )
        }
    }
}
