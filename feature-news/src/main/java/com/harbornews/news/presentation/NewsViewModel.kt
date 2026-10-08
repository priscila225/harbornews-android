package com.harbornews.news.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.harbornews.common.BaseViewModel
import com.harbornews.common.DispatcherProvider
import com.harbornews.common.Event
import com.harbornews.news.domain.GetHeadlinesUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NewsViewModel(
    private val getHeadlines: GetHeadlinesUseCase,
    dispatchers: DispatcherProvider,
) : BaseViewModel(dispatchers) {
    private val _state = MutableLiveData<NewsViewState>()
    val state: LiveData<NewsViewState> = _state

    private val _events = MutableLiveData<Event<NewsEvent>>()
    val events: LiveData<Event<NewsEvent>> = _events

    private val _section = MutableLiveData<String?>(null)
    val section: LiveData<String?> = _section

    private var loadJob: Job? = null

    fun load() {
        loadJob?.cancel()
        _state.value = NewsViewState.Loading
        loadJob = launch(dispatchers.ui) {
            getHeadlines.execute(_section.value).fold(
                onSuccess = { _state.value = NewsViewState.Success(it) },
                onFailure = {
                    _state.value = NewsViewState.Error(it.message ?: "Unable to load headlines")
                    _events.value = Event(NewsEvent.ShowMessage("Headlines unavailable"))
                },
            )
        }
    }

    /** Switches the section filter; selecting the section that is already shown does nothing. */
    fun selectSection(section: String?) {
        if (section == _section.value) return
        _section.value = section
        load()
    }
}
