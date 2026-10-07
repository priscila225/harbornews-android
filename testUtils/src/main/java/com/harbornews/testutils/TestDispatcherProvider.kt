package com.harbornews.testutils

import com.harbornews.common.DispatcherProvider
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher

/** [bg] defaults to the same dispatcher as [ui]; pass another one to keep background work off the test scheduler. */
class TestDispatcherProvider(
    val testDispatcher: TestDispatcher = StandardTestDispatcher(),
    override val bg: CoroutineDispatcher = testDispatcher,
) : DispatcherProvider {
    override val ui: CoroutineDispatcher = testDispatcher
}
