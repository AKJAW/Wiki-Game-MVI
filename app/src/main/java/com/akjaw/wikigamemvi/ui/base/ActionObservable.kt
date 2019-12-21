package com.akjaw.wikigamemvi.ui.base

import io.reactivex.Observable


interface ActionObservable<A> {
    fun events(): Observable<A>
}