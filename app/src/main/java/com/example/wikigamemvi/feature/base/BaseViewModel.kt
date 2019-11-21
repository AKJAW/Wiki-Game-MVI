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

abstract class BaseViewModel<A: BaseAction, R: BaseResult, S: BaseViewState, E: BaseViewEffect>
    : ViewModel(){

    protected val compositeDisposable = CompositeDisposable()

    private val actions = PublishRelay.create<A>()

    private val store: Observable<Lce<out R>> by lazy {
        actions.actionToResult()
            .share()
    }

    val viewState: Observable<S> by lazy {
        store.resultToViewState()
            .replay()
            .autoConnect(1) { compositeDisposable += it }
    }

    val viewEffects: Observable<E> by lazy {
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