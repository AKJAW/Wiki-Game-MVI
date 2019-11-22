package com.example.wikigamemvi.feature.game.model

import com.example.wikigamemvi.data.model.WikiArticle
import com.example.wikigamemvi.data.model.WikiResponse
import com.example.wikigamemvi.data.model.WikiUrl
import com.example.wikigamemvi.feature.base.*

data class GameViewState(
    val targetArticle: WikiArticle? = null,
    val isTargetArticleLoading: Boolean = false,
    val currentArticle: WikiArticle? = null,
    val isCurrentArticleLoading: Boolean = false,
    val navigationUrls: List<WikiUrl> = listOf()
): BaseViewState

sealed class GameViewEffect: BaseViewEffect{
    data class SomeToastEffect(val text: String): GameViewEffect()
}

sealed class GameAction: BaseAction {
    data class ShowToastAction(val text: String): GameAction()
    object InitializeArticlesAction: GameAction()
//    object LoadTargetArticleAction: GameAction()
}

sealed class GameResult: BaseResult {
    data class LoadTargetArticleResult(val articleResponse: WikiResponse): GameResult()
    data class LoadCurrentArticleResult(val articleResponse: WikiResponse): GameResult()

    data class InitializeArticlesResult(
        val targetArticleResponse: WikiResponse,
        val currentArticleResponse: WikiResponse
    ): GameResult()  {
        companion object: LoadingResult<InitializeArticlesResult>{
            override fun getLoadingResult(): InitializeArticlesResult {
                val response = WikiResponse("", "", "", "", listOf())
                return InitializeArticlesResult(response, response)
            }
        }
    }

    data class ShowToastResult(val text: String): GameResult()
}