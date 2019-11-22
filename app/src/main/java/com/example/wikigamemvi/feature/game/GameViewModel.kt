package com.example.wikigamemvi.feature.game

import com.example.wikigamemvi.data.model.WikiArticle
import com.example.wikigamemvi.data.model.WikiResponse
import com.example.wikigamemvi.feature.base.BaseViewModel
import com.example.wikigamemvi.feature.base.Lce
import com.example.wikigamemvi.feature.game.model.GameAction
import com.example.wikigamemvi.feature.game.model.GameAction.*
import com.example.wikigamemvi.feature.game.model.GameResult
import com.example.wikigamemvi.feature.game.model.GameResult.*
import com.example.wikigamemvi.feature.game.model.GameViewEffect
import com.example.wikigamemvi.feature.game.model.GameViewEffect.SomeToastEffect
import com.example.wikigamemvi.feature.game.model.GameViewState
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class GameViewModel(): BaseViewModel<GameAction, GameResult, GameViewState, GameViewEffect>(){

    val testArticle = WikiResponse("name", "Description", "img.jpg", "wiki.pl", listOf())
    fun loadArticle() = Observable
        .just(testArticle)
        .delay(1L, TimeUnit.SECONDS)

    init {
        process(InitializeArticlesAction)
    }

    override fun Observable<GameAction>.actionToResult(): Observable<Lce<out GameResult>> {
        return publish <Lce<out GameResult>> {
            Observable.merge(
                it.ofType<ShowToastAction>().onShowToast(),
                it.ofType<InitializeArticlesAction>().onInitializeArticles()
            )
        }
    }

    override fun Observable<Lce<out GameResult>>.resultToViewState(): Observable<GameViewState> {
        return scan(GameViewState()) { state, result ->
            when(result){
                is Lce.Content -> handleResultContent(state, result.payload)
                is Lce.Loading -> handleResultLoading(state, result.payload)
                is Lce.Error -> TODO()
            }
        }.distinctUntilChanged()
    }

    private fun handleResultContent(state: GameViewState, payload: GameResult): GameViewState {
        return when(payload){
            is InitializeArticlesResult -> {
                val targetArticle = WikiArticle.fromResponse(payload.targetArticleResponse)
                val currentArticle = WikiArticle.fromResponse(payload.currentArticleResponse)

                state.copy(
                    targetArticle = targetArticle,
                    isTargetArticleLoading = false,
                    currentArticle = currentArticle,
                    isCurrentArticleLoading = false)
            }
            is LoadTargetArticleResult -> {
                val name = payload.articleResponse.name
                val wikiArticle = WikiArticle(name, "", "")
                state.copy(targetArticle = wikiArticle, isTargetArticleLoading = false)
            }
            else -> state.copy()
        }
    }

    private fun handleResultLoading(
        state: GameViewState, payload: GameResult
    ): GameViewState {
       return when(payload){
            is InitializeArticlesResult -> state.copy(
                targetArticle = null,
                isTargetArticleLoading = true,
                currentArticle = null,
                isCurrentArticleLoading = true)
           else -> state.copy()
       }
    }

    override fun Observable<Lce<out GameResult>>.resultToViewEffect(): Observable<GameViewEffect> {
        val s = "s"
        return filter { it is Lce.Content && it.payload is ShowToastResult }
            .map<GameViewEffect> {
                val showToast = it as Lce.Content<ShowToastResult>
                SomeToastEffect(showToast.payload.text)
            }
    }


    private fun Observable<InitializeArticlesAction>.onInitializeArticles(): Observable<Lce<InitializeArticlesResult>> {
        return switchMap<Lce<InitializeArticlesResult>> {
            Observable.zip<WikiResponse, WikiResponse, List<WikiResponse>>(
                loadArticle().observeOn(Schedulers.io()),
                loadArticle().observeOn(Schedulers.io()),
                BiFunction { t1, t2 -> listOf(t1, t2) }
            )
                .map<Lce<InitializeArticlesResult>> {
                    Lce.Content(InitializeArticlesResult(it[0], it[1]))
                }
                .onErrorReturn {
                    val errorResult = InitializeArticlesResult(
                        testArticle.copy(name = "Error1"),
                        testArticle.copy(name = "Error2"))

                    Lce.Error(errorResult)
                }
                .startWith(Lce.Loading(InitializeArticlesResult.getLoadingResult()))
        }
    }

    private fun Observable<ShowToastAction>.onShowToast(): Observable<Lce<ShowToastResult>> {
        return map<Lce<ShowToastResult>> { Lce.Content(ShowToastResult("aa")) }
    }
}
