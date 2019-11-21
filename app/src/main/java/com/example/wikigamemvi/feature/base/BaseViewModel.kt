package com.example.wikigamemvi.feature.base

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

interface BaseAction
interface BaseViewState
interface BaseViewEffect
interface BaseResult

abstract class BaseViewModel<A: BaseAction, S: BaseViewState, E: BaseViewEffect, R: BaseResult>
    : ViewModel(){

    protected val compositeDisposable = CompositeDisposable()

    private val actions = PublishRelay.create<A>()

    private val store by lazy {
        actions.actionToResult()
            .share()
    }

    protected val viewState: Observable<S> by lazy {
        store.resultToViewState()
            .replay()
            .autoConnect(1) { compositeDisposable += it }
    }

    protected val viewEffects: Observable<E> by lazy {
        store.resultToViewEffect()
    }

    fun process(action: A) {
        actions.accept(action)
    }

    override fun onCleared() {
        super.onCleared()

        compositeDisposable.clear()
    }

    abstract fun Observable<A>.actionToResult(): Observable<Lce<out R>>

    abstract fun Observable<Lce<out R>>.resultToViewState(): Observable<S>

    //TODO make a default implementation block so it doesnt have to be always implemented
    abstract fun Observable<Lce<out R>>.resultToViewEffect(): Observable<E>
}