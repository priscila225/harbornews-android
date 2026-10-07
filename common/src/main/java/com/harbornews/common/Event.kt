package com.harbornews.common

/** One-shot wrapper for LiveData values that must be handled once (navigation, toasts). */
class Event<out T>(private val content: T) {
    private var handled = false

    fun consume(): T? = if (handled) {
        null
    } else {
        handled = true
        content
    }
}
