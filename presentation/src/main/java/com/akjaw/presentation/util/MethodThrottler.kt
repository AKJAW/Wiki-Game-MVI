package com.akjaw.presentation.util

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.util.concurrent.TimeUnit

class MethodThrottler<I, R>(private val duration: Long = 500L, private val passedInOnClick: (I) -> R){
    private val relay = PublishRelay.create<I>()

    val disposable: Disposable = createObservable()
        .onErrorResumeNext(createObservable())
        .subscribe()

    private fun createObservable(): Observable<I> {
        return relay
            .throttleFirst(duration, TimeUnit.MILLISECONDS)
            .doOnNext {
                passedInOnClick(it)
            }
    }


    fun onClick(input: I){
        relay.accept(input)
    }
}