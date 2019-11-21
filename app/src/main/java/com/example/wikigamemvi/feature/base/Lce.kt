package com.example.wikigamemvi.feature.base

sealed class Lce<T> {
    class Loading<T> : Lce<T>()
    data class Content<T>(val payload: T) : Lce<T>()
    data class Error<T>(val payload: T) : Lce<T>()
}
