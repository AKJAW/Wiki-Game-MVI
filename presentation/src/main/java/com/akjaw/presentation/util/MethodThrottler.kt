package com.akjaw.presentation.util

import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class MethodThrottler<I, R>(
    compositeDisposable: CompositeDisposable,
    private val duration: Long = 500L,
    private val passedInOnClick: (I) -> R
){

    private val relay = PublishRelay.create<I>()

    init {
        createOnClickObservable()
            .onErrorResumeNext(createOnClickObservable())
            .subscribe()
            .addTo(compositeDisposable)
    }

    private fun createOnClickObservable(): Observable<I> {
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
