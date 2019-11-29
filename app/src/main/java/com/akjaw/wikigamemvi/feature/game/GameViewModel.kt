package com.akjaw.wikigamemvi.feature.game

import com.akjaw.domain.model.WikiTitle
import com.akjaw.domain.usecase.ArticleWinConditionUseCase
import com.akjaw.domain.usecase.GetArticleFromTitleUseCase
import com.akjaw.domain.usecase.InitializeArticlesUseCase
import com.akjaw.wikigamemvi.feature.base.BaseViewModel
import com.akjaw.wikigamemvi.feature.base.Lce
import com.akjaw.wikigamemvi.feature.game.model.GameAction
import com.akjaw.wikigamemvi.feature.game.model.GameAction.*
import com.akjaw.wikigamemvi.feature.game.model.GameResult
import com.akjaw.wikigamemvi.feature.game.model.GameResult.*
import com.akjaw.wikigamemvi.feature.game.model.GameViewEffect
import com.akjaw.wikigamemvi.feature.game.model.GameViewEffect.SomeToastEffect
import com.akjaw.wikigamemvi.feature.game.model.GameViewState
import com.akjaw.wikigamemvi.util.toArticle
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val initializeArticlesUseCase: InitializeArticlesUseCase,
    private val getArticleFromTitleUseCase: GetArticleFromTitleUseCase,
    private val winConditionUseCase: ArticleWinConditionUseCase
): BaseViewModel<GameAction, GameResult, GameViewState, GameViewEffect>(){

    init {
        process(InitializeArticlesAction)
    }

    override fun Observable<GameAction>.actionToResult(): Observable<Lce<out GameResult>> {
        return publish <Lce<out GameResult>> {
            Observable.merge(
                it.ofType<ShowToastAction>().onShowToast(),
                it.ofType<InitializeArticlesAction>().onInitializeArticles(),
                it.ofType<LoadNextArticleAction>().onLoadNextArticle()
            )
        }
    }

    override fun Observable<Lce<out GameResult>>.resultToViewState(): Observable<GameViewState> {
        return scan(GameViewState()) { state, result ->
            when(result){
                is Lce.Content -> handleResultContent(state, result.payload)
                is Lce.Loading -> handleResultLoading(state, result.payload)
                is Lce.Error -> handleResultError(state, result.payload)
            }
        }.distinctUntilChanged()
    }

    private fun handleResultContent(state: GameViewState, payload: GameResult): GameViewState {
        return when(payload){
            is InitializeArticlesResult -> {
                val targetArticle = payload.targetArticleResponse.toArticle()
                val currentArticle = payload.currentArticleResponse.toArticle()

                state.copy(
                    targetArticle = targetArticle,
                    isTargetArticleLoading = false,
                    currentArticle = currentArticle,
                    isCurrentArticleLoading = false,
                    wikiNavigationLinks = payload.currentArticleResponse.outgoingTitles,
                    hasWon = false)
            }

            is LoadNextArticleResult -> {
                val currentArticle = payload.articleResponse.toArticle()

                state.copy(
                    currentArticle = currentArticle,
                    isCurrentArticleLoading = false,
                    wikiNavigationLinks = payload.articleResponse.outgoingTitles)
            }

            is WinConditionResult -> {
                state.copy(
                    hasWon = true
                )
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

            is LoadNextArticleResult -> state.copy(
                currentArticle = null,
                isCurrentArticleLoading = true,
                wikiNavigationLinks = listOf()
            )

            else -> state.copy()
        }
    }

    private fun handleResultError(state: GameViewState, payload: GameResult): GameViewState {
        return when(payload){
            is InitializeArticlesResult -> state.copy(
                targetArticle = null,
                isTargetArticleLoading = true,
                currentArticle = null,
                isCurrentArticleLoading = true,
                wikiNavigationLinks = listOf())

            is LoadNextArticleResult -> state.copy(
                currentArticle = null,
                isCurrentArticleLoading = true,
                wikiNavigationLinks = listOf()
            )

            else -> state.copy()
        }
    }

    override fun Observable<Lce<out GameResult>>.resultToViewEffect(): Observable<GameViewEffect> {
        return filter { it is Lce.Content }
            .cast(Lce.Content::class.java)
            .flatMap <GameViewEffect> { content ->
                when(content.payload){
                    is ShowToastResult -> {
                        Observable.just(
                            SomeToastEffect(content.payload.text)
                        )
                    }
                    else -> Observable.empty<GameViewEffect>()
                }
            }
    }


    private fun Observable<ShowToastAction>.onShowToast(): Observable<Lce<ShowToastResult>> {
        return map<Lce<ShowToastResult>> { Lce.Content(ShowToastResult(it.text)) }
    }

    private fun Observable<InitializeArticlesAction>.onInitializeArticles(): Observable<Lce<InitializeArticlesResult>> {
        return switchMap {
            initializeArticlesUseCase()
                .map<Lce<InitializeArticlesResult>> { responses ->
                    Lce.Content(InitializeArticlesResult(responses.first, responses.second))
                }
                .onErrorReturn {
                    val errorResult = InitializeArticlesResult()

                    Lce.Error(errorResult)
                }
                .startWith(Lce.Loading(InitializeArticlesResult()))
        }
    }

    private fun Observable<LoadNextArticleAction>.onLoadNextArticle(): Observable<Lce<GameResult>> {
        return switchMap {
            winConditionUseCase(it.wikiTitle)
                .map <Lce<GameResult>> {
                    Lce.Content(WinConditionResult)
                }
                .onErrorReturn {
                    Lce.Error(WinConditionResult)
                }
                .switchIfEmpty(getNextArticle(it.wikiTitle) as Observable<Lce<GameResult>>)
        }
    }

    private fun getNextArticle(wikiTitle: WikiTitle): Observable<Lce<LoadNextArticleResult>> {
        return getArticleFromTitleUseCase(wikiTitle)
            .map<Lce<LoadNextArticleResult>> { response ->
                Lce.Content(LoadNextArticleResult(response))
            }
            .onErrorReturn {
                Lce.Error(LoadNextArticleResult())
            }
            .startWith(Lce.Loading(LoadNextArticleResult()))
    }


}