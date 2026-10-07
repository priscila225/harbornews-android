package com.harbornews.common

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

interface DispatcherProvider {
    val ui: CoroutineDispatcher
    val bg: CoroutineDispatcher
}

class DispatcherProviderImpl : DispatcherProvider {
    override val ui: CoroutineDispatcher = Dispatchers.Main
    override val bg: CoroutineDispatcher = Dispatchers.IO
}
