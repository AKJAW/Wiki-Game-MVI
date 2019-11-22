package com.example.wikigamemvi.feature.base

import io.reactivex.Observable


interface ActionObservable<A> {
    fun events(): Observable<A>
}