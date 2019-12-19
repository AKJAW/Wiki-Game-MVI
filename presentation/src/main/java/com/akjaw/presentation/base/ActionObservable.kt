package com.akjaw.presentation.base

import io.reactivex.Observable


interface ActionObservable<A> {
    fun events(): Observable<A>
}