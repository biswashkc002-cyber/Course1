package com.kcbiswash.course1.data

sealed class Result<out T> {
    // Neutral “do nothing yet” state so UI stays interactive at first
    object Idle : Result<Nothing>()

    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}

