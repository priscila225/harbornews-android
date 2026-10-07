package com.harbornews.testutils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

/** Collects every value a LiveData emits until [block] finishes. */
fun <T> LiveData<T>.captureValues(block: () -> Unit): List<T> {
    val values = mutableListOf<T>()
    val observer = Observer<T> { values.add(it) }
    observeForever(observer)
    try {
        block()
    } finally {
        removeObserver(observer)
    }
    return values
}
