package com.akjaw.wikigamemvi.util

import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class MethodThrottler<I, R>(duration: Long = 500L, passedInOnClick: (I) -> R){
    private val subject = PublishSubject.create<I>()

    val disposable: Disposable = subject
        .throttleFirst(duration, TimeUnit.MILLISECONDS)
        .subscribe {
            passedInOnClick(it)
        }

    fun onClick(input: I){
        subject.onNext(input)
    }
}