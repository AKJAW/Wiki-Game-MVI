package com.example.wikigamemvi.feature.game.model

import com.example.wikigamemvi.data.model.WikiArticle
import com.example.wikigamemvi.data.model.WikiResponse
import com.example.wikigamemvi.data.model.WikiTitle
import com.example.wikigamemvi.feature.base.*

data class GameViewState(
    val targetArticle: WikiArticle? = null,
    val isTargetArticleLoading: Boolean = false,
    val currentArticle: WikiArticle? = null,
    val isCurrentArticleLoading: Boolean = false,
    val wikiNavigationLinks: List<WikiTitle> = listOf()
): BaseViewState

sealed class GameViewEffect: BaseViewEffect{
    data class SomeToastEffect(val text: String): GameViewEffect()
}

sealed class GameAction: BaseAction {
    data class ShowToastAction(val text: String): GameAction()
    object InitializeArticlesAction: GameAction()
    object LoadCurrentArticleAction: GameAction()
}

sealed class GameResult: BaseResult {
    data class ShowToastResult(val text: String): GameResult()

    data class InitializeArticlesResult(
        val targetArticleResponse: WikiResponse = WikiResponse(),
        val currentArticleResponse: WikiResponse = WikiResponse()
    ): GameResult()

    data class LoadCurrentArticleResult(
        val articleResponse: WikiResponse = WikiResponse()
    ): GameResult()
}