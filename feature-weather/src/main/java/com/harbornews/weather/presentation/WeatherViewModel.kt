package com.harbornews.weather.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harbornews.weather.domain.GetCurrentConditionsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val getCurrentConditions: GetCurrentConditionsUseCase,
) : ViewModel() {
    private val _state = MutableLiveData<WeatherViewState>(WeatherViewState.Loading)
    val state: LiveData<WeatherViewState> = _state

    fun refresh() {
        viewModelScope.launch(Dispatchers.Main) {
            Log.d(TAG, "refresh requested")
            getCurrentConditions.execute().fold(
                onSuccess = {
                    _state.value = WeatherViewState.Success(it)
                    Log.d(TAG, "refresh succeeded")
                },
                onFailure = { _state.value = WeatherViewState.Unavailable },
            )
        }
    }

    private companion object {
        const val TAG = "WeatherViewModel"
    }
}
