package com.harbornews.testutils

import com.harbornews.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

class TestDispatcherProvider(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
) : DispatcherProvider {
    override val ui: CoroutineDispatcher = testDispatcher
    override val bg: CoroutineDispatcher = testDispatcher
}
