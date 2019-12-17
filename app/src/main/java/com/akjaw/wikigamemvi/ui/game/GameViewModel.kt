package com.akjaw.wikigamemvi.ui.game

import com.akjaw.domain.model.ArticleType
import com.akjaw.domain.model.WikiTitle
import com.akjaw.domain.usecase.ArticleWinConditionUseCase
import com.akjaw.domain.usecase.GetArticleFromTitleUseCase
import com.akjaw.domain.usecase.InitializeArticlesUseCase
import com.akjaw.wikigamemvi.ui.base.BaseViewModel
import com.akjaw.wikigamemvi.ui.base.Lce
import com.akjaw.wikigamemvi.ui.game.model.GameAction
import com.akjaw.wikigamemvi.ui.game.model.GameAction.*
import com.akjaw.wikigamemvi.ui.game.model.GameResult
import com.akjaw.wikigamemvi.ui.game.model.GameResult.*
import com.akjaw.wikigamemvi.ui.game.model.GameViewEffect
import com.akjaw.wikigamemvi.ui.game.model.GameViewEffect.ShowVictoryScreenEffect
import com.akjaw.wikigamemvi.ui.game.model.GameViewState
import com.akjaw.wikigamemvi.util.toArticle
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class GameViewModel @Inject constructor(
    private val initializeArticlesUseCase: InitializeArticlesUseCase,
    private val getArticleFromTitleUseCase: GetArticleFromTitleUseCase,
    private val winConditionUseCase: ArticleWinConditionUseCase,
    private val initialState: GameViewState = GameViewState()
): BaseViewModel<GameAction, GameResult, GameViewState, GameViewEffect>(){
    private var isInitialized = false

    fun initialize(){
        if(!isInitialized){
            process(InitializeArticlesAction)
            isInitialized = true
        }
    }

    override fun Observable<GameAction>.actionToResult(): Observable<Lce<out GameResult>> {
        return publish <Lce<out GameResult>> {

            val observablesToBeMerged = listOf(
                it.ofType<InitializeArticlesAction>().onInitializeArticles(),
                it.ofType<LoadNextArticleAction>().onLoadNextArticle(),
                it.ofType<ToggleArticleModeAction>().map { action ->
                    val result = ToggleArticleModeResult(action.type)
                    Lce.Content(result)
                }
            )

            Observable.merge(observablesToBeMerged)
        }
    }

    override fun Observable<Lce<out GameResult>>.resultToViewState(): Observable<GameViewState> {
        return scan(initialState) { state, result ->
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

                val targetArticleState = state.targetArticleState.copy(
                    article = targetArticle,
                    isLoading = false
                )

                val currentArticleState = state.targetArticleState.copy(
                    article = currentArticle,
                    isLoading = false
                )

                state.copy(
                    targetArticleState = targetArticleState,
                    currentArticleState = currentArticleState,
                    wikiNavigationLinks = payload.currentArticleResponse.outgoingTitles)
            }

            is LoadNextArticleResult -> {
                val currentArticle = payload.articleResponse.toArticle()

                val currentArticleState = state.targetArticleState.copy(
                    article = currentArticle,
                    isLoading = false
                )

                state.copy(
                    currentArticleState = currentArticleState,
                    numberOfSteps = state.numberOfSteps + 1,
                    wikiNavigationLinks = payload.articleResponse.outgoingTitles)
            }

            is ToggleArticleModeResult -> {
                when(payload.type){
                    ArticleType.CURRENT -> {
                        if(state.currentArticleState.isLoading){
                            state.copy()
                        } else {
                            val currentArticleState = state.currentArticleState.copy(
                                mode = state.currentArticleState.mode.inverted()
                            )
                            state.copy(currentArticleState = currentArticleState)
                        }
                    }
                    ArticleType.TARGET -> {
                        if(state.targetArticleState.isLoading){
                            state.copy()
                        } else {
                            val targetArticleState = state.targetArticleState.copy(
                                mode = state.targetArticleState.mode.inverted()
                            )
                            state.copy(targetArticleState = targetArticleState)
                        }
                    }
                }
            }

            else -> state.copy()
        }
    }

    private fun handleResultLoading(
        state: GameViewState, payload: GameResult
    ): GameViewState {
        return when(payload){
            is InitializeArticlesResult -> {

                val targetArticleState = state.targetArticleState.copy(
                    article = null,
                    isLoading = true
                )

                val currentArticleState = state.targetArticleState.copy(
                    article = null,
                    isLoading = true
                )

                state.copy(
                    targetArticleState = targetArticleState,
                    currentArticleState = currentArticleState,
                    wikiNavigationLinks = listOf())
            }

            is LoadNextArticleResult -> {
                val currentArticleState = state.targetArticleState.copy(
                    article = null,
                    isLoading = true
                )

                state.copy(
                    currentArticleState = currentArticleState,
                    wikiNavigationLinks = listOf()
                )
            }

            else -> state.copy()
        }
    }

    private fun handleResultError(state: GameViewState, payload: GameResult): GameViewState {
        return when(payload){
            is InitializeArticlesResult -> {

                val targetArticleState = state.targetArticleState.copy(
                    article = null,
                    isLoading = false,
                    hasError = true
                )

                val currentArticleState = state.targetArticleState.copy(
                    article = null,
                    isLoading = false,
                    hasError = true
                )

                return state.copy(
                    targetArticleState = targetArticleState,
                    currentArticleState = currentArticleState
                )
            }
            is LoadNextArticleResult -> {

                val currentArticleState = state.targetArticleState.copy(
                    article = null,
                    isLoading = false,
                    hasError = true
                )

                return state.copy(
                    currentArticleState = currentArticleState
                )
            }
            else -> state.copy()
        }
    }

    override fun Observable<Lce<out GameResult>>.resultToViewEffect(): Observable<GameViewEffect> {
        return filter { it is Lce.Content }
            .cast(Lce.Content::class.java)
            .flatMap <GameViewEffect> { content ->
                val viewEffect: GameViewEffect? = when(content.payload){
                    is ShowVictoryScreenResult -> ShowVictoryScreenEffect
                    else -> null
                }

                if(viewEffect == null){
                    Observable.empty()
                } else {
                    Observable.just(viewEffect)
                }

            }
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
            @Suppress("UNCHECKED_CAST")
            winConditionUseCase(it.wikiTitle)
                .map <Lce<GameResult>> {
                    Lce.Content(ShowVictoryScreenResult)
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