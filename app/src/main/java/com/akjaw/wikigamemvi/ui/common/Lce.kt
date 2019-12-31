package com.akjaw.wikigamemvi.ui.common

sealed class Lce<T> {
    data class Loading<T>(val payload: T) : Lce<T>()
    data class Content<T>(val payload: T) : Lce<T>()
    data class Error<T>(val payload: T) : Lce<T>()
}
