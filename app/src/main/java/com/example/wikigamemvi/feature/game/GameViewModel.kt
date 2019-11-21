package com.example.wikigamemvi.feature.game

import com.example.wikigamemvi.data.model.WikiArticle
import com.example.wikigamemvi.data.model.WikiResponse
import com.example.wikigamemvi.feature.base.BaseViewModel
import com.example.wikigamemvi.feature.base.Lce
import com.example.wikigamemvi.feature.game.model.GameAction
import com.example.wikigamemvi.feature.game.model.GameAction.LoadTargetArticleAction
import com.example.wikigamemvi.feature.game.model.GameResult
import com.example.wikigamemvi.feature.game.model.GameResult.LoadTargetArticleResult
import com.example.wikigamemvi.feature.game.model.GameViewEffect
import com.example.wikigamemvi.feature.game.model.GameViewState
import io.reactivex.Observable
import io.reactivex.rxkotlin.ofType
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class GameViewModel(): BaseViewModel<GameAction, GameResult, GameViewState, GameViewEffect>(){

    val testArticle = WikiResponse("name", "Description", "img.jpg", "wiki.pl", listOf())
    fun loadArticle() = Observable
        .just(testArticle)
        .delay(1L, TimeUnit.SECONDS)

    override fun Observable<GameAction>.actionToResult(): Observable<Lce<out GameResult>> {
        return publish {
             it.ofType<LoadTargetArticleAction>().onLoadArticle().map<Lce<out GameResult>> { it as Lce<GameResult> }
        }
    }

    override fun Observable<Lce<out GameResult>>.resultToViewState(): Observable<GameViewState> {
        return scan(GameViewState()) { state, result ->
            when(result){
                is Lce.Content -> handleResultContent(state, result.payload)
                is Lce.Loading -> handleResultLoading(state)
                is Lce.Error -> TODO()
            }
        }.distinctUntilChanged()
    }

    private fun handleResultContent(state: GameViewState, payload: GameResult): GameViewState {
        return when(payload){
            is LoadTargetArticleResult -> {
                val name = payload.articleResponse.name
                val wikiArticle = WikiArticle(name, "", "")
                state.copy(targetArticle = wikiArticle, isTargetArticleLoading = false)
            }
        }
    }

    private fun handleResultLoading(state: GameViewState): GameViewState {
        return state.copy(targetArticle = null, isTargetArticleLoading = true)
    }

    override fun Observable<Lce<out GameResult>>.resultToViewEffect(): Observable<GameViewEffect> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private fun Observable<LoadTargetArticleAction>.onLoadArticle(): Observable<Lce<LoadTargetArticleResult>> {
        return switchMap<Lce<LoadTargetArticleResult>> {
            loadArticle()
                .subscribeOn(Schedulers.io())
                .map<Lce<LoadTargetArticleResult>> {
                    Lce.Content(LoadTargetArticleResult(it))
                }
                .onErrorReturn {
                    Lce.Error(LoadTargetArticleResult(testArticle.copy(name = "Error")))
                }
                .startWith(Lce.Loading())
        }
    }
}
