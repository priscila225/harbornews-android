package com.harbornews.common

import kotlinx.coroutines.CancellationException

sealed class Result<out T> {
    data class Success<out T>(val value: T) : Result<T>()

    data class Failure(val error: Throwable) : Result<Nothing>()

    inline fun <R> fold(onSuccess: (T) -> R, onFailure: (Throwable) -> R): R = when (this) {
        is Success -> onSuccess(value)
        is Failure -> onFailure(error)
    }

    companion object {
        inline fun <T> runCatching(block: () -> T): Result<T> = try {
            Success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (
            @Suppress("TooGenericExceptionCaught") e: Exception
        ) {
            Failure(e)
        }
    }
}
