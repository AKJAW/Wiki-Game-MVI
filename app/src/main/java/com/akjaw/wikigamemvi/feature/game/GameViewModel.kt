package com.akjaw.wikigamemvi.feature.game

import com.akjaw.wikigamemvi.data.model.WikiArticle
import com.akjaw.wikigamemvi.data.model.WikiResponse
import com.akjaw.wikigamemvi.data.repository.base.WikipediaRepository
import com.akjaw.wikigamemvi.feature.base.BaseViewModel
import com.akjaw.wikigamemvi.feature.base.Lce
import com.akjaw.wikigamemvi.feature.game.model.GameAction
import com.akjaw.wikigamemvi.feature.game.model.GameAction.*
import com.akjaw.wikigamemvi.feature.game.model.GameResult
import com.akjaw.wikigamemvi.feature.game.model.GameResult.*
import com.akjaw.wikigamemvi.feature.game.model.GameViewEffect
import com.akjaw.wikigamemvi.feature.game.model.GameViewEffect.SomeToastEffect
import com.akjaw.wikigamemvi.feature.game.model.GameViewState
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val wikipediaRepository: WikipediaRepository
): BaseViewModel<GameAction, GameResult, GameViewState, GameViewEffect>(){

    val testArticle = WikiResponse("name", "Description", "img.jpg", "wiki.pl", listOf("aa", "bb", "cc"))
    var i = 0
    fun loadArticle() = Observable
        .just(testArticle.copy(name = testArticle.name + i++))
        .delay(3L, TimeUnit.SECONDS)

    init {
        process(InitializeArticlesAction)
    }

    override fun Observable<GameAction>.actionToResult(): Observable<Lce<out GameResult>> {
        return publish <Lce<out GameResult>> {
            Observable.merge(
                it.ofType<ShowToastAction>().onShowToast(),
                it.ofType<InitializeArticlesAction>().onInitializeArticles(),
                it.ofType<LoadCurrentArticleAction>().onLoadCurrentArticle()
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
                    isCurrentArticleLoading = false,
                    wikiNavigationLinks = payload.currentArticleResponse.outgoingTitles)
            }

            is LoadCurrentArticleResult -> {
                val currentArticle = WikiArticle.fromResponse(payload.articleResponse)

                state.copy(
                    currentArticle = currentArticle,
                    isCurrentArticleLoading = false,
                    wikiNavigationLinks = payload.articleResponse.outgoingTitles)
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
                isCurrentArticleLoading = true,
                wikiNavigationLinks = listOf())

            is LoadCurrentArticleResult -> state.copy(
                currentArticle = null,
                isCurrentArticleLoading = true,
                wikiNavigationLinks = listOf()
            )

            else -> state.copy()
        }
    }

    override fun Observable<Lce<out GameResult>>.resultToViewEffect(): Observable<GameViewEffect> {
        return filter { it is Lce.Content && it.payload is ShowToastResult }
            .map<GameViewEffect> {
                val showToast = it as Lce.Content<ShowToastResult>
                SomeToastEffect(showToast.payload.text)
            }
    }


    private fun Observable<ShowToastAction>.onShowToast(): Observable<Lce<ShowToastResult>> {
        return map<Lce<ShowToastResult>> { Lce.Content(ShowToastResult("aa")) }
    }

    private fun Observable<InitializeArticlesAction>.onInitializeArticles(): Observable<Lce<InitializeArticlesResult>> {
        return switchMap {
            Observable.zip<WikiResponse, WikiResponse, List<WikiResponse>>(
                loadArticle().observeOn(Schedulers.io()),
                loadArticle().observeOn(Schedulers.io()),
                BiFunction { t1, t2 -> listOf(t1, t2) }
            )
                .map<Lce<InitializeArticlesResult>> { responses ->
                    Lce.Content(InitializeArticlesResult(responses[0], responses[1]))
                }
                .onErrorReturn {
                    val errorResult = InitializeArticlesResult()

                    Lce.Error(errorResult)
                }
                .startWith(Lce.Loading(InitializeArticlesResult()))
        }
    }

    private fun Observable<LoadCurrentArticleAction>.onLoadCurrentArticle(): Observable<Lce<LoadCurrentArticleResult>> {
        return switchMap {
            loadArticle()
                .map<Lce<LoadCurrentArticleResult>> { response ->
                    Lce.Content(LoadCurrentArticleResult(response))
                }
                .onErrorReturn {
                    Lce.Error(LoadCurrentArticleResult())
                }
                .startWith(Lce.Loading(LoadCurrentArticleResult()))
        }
    }

}