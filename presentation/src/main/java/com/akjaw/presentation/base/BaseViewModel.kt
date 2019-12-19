package com.akjaw.presentation.base

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import timber.log.Timber

interface BaseAction
interface BaseViewState
interface BaseViewEffect
interface BaseResult

abstract class BaseViewModel<A: BaseAction, R: BaseResult, S: BaseViewState, E: BaseViewEffect>
    : ViewModel(){

    protected val disposables = CompositeDisposable()

    private val actions = BehaviorRelay.create<A>()

    private val store: Observable<Lce<out R>> by lazy {
        actions.actionToResult()
            .share()
    }

    val viewState: Observable<S> by lazy {
        store.resultToViewState()
            .doOnNext { Timber.tag("-----").d("vs $it") }
            .observeOn(AndroidSchedulers.mainThread())
            .replay()
            .autoConnect(1) { disposables += it }
    }

    val viewEffects: Observable<E> by lazy {
        store.doOnNext { Timber.tag("-----").d("ve before $it") }
            .resultToViewEffect()
            .doOnNext { Timber.tag("-----").d("ve $it") }
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun process(action: A) {
        Timber.tag("-----").d("process $action")
        actions.accept(action)
    }

    override fun onCleared() {
        super.onCleared()

        disposables.clear()
    }

    abstract fun Observable<A>.actionToResult(): Observable<Lce<out R>>

    abstract fun Observable<Lce<out R>>.resultToViewState(): Observable<S>

    //TODO make a default implementation block so it doesnt have to be always implemented
    abstract fun Observable<Lce<out R>>.resultToViewEffect(): Observable<E>
}