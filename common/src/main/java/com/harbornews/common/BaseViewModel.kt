package com.harbornews.common

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlin.coroutines.CoroutineContext

/**
 * Base class for all ViewModels. Coroutines are launched on `this` so that the injected
 * [DispatcherProvider] decides the threading and every child is cancelled in [onCleared].
 */
abstract class BaseViewModel(protected val dispatchers: DispatcherProvider) : ViewModel(), CoroutineScope {
    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.ui + job

    override fun onCleared() {
        job.cancelChildren()
        job.cancel()
        super.onCleared()
    }
}
