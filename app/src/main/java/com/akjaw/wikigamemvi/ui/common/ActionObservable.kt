package com.akjaw.wikigamemvi.ui.common

import io.reactivex.Observable


interface ActionObservable<A> {
    fun actions(): Observable<A>
}