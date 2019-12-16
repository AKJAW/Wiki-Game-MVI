package com.akjaw.wikigamemvi.ui.game.model

import com.akjaw.domain.model.ArticleType
import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import com.akjaw.wikigamemvi.ui.base.*
import com.akjaw.wikigamemvi.ui.common.ArticleView.ArticleViewMode

data class ArticleState(
    val article: WikiArticle? = null,
    val isLoading: Boolean = false,
    val mode: ArticleViewMode = ArticleViewMode.COLLAPSED
)

data class GameViewState(
    val targetArticleState: ArticleState = ArticleState(),
    val currentArticleState: ArticleState = ArticleState(),
    val numberOfSteps: Int = 0,
    val wikiNavigationLinks: List<WikiTitle> = listOf()
): BaseViewState

sealed class GameViewEffect: BaseViewEffect{
    object ShowVictoryScreenEffect: GameViewEffect()
}

sealed class GameAction: BaseAction {
    object InitializeArticlesAction: GameAction()
    data class LoadNextArticleAction(val wikiTitle: WikiTitle): GameAction()
    data class ToggleArticleModeAction(val type: ArticleType): GameAction()
}

sealed class GameResult: BaseResult {
    data class InitializeArticlesResult(
        val targetArticleResponse: WikiResponse = WikiResponse(),
        val currentArticleResponse: WikiResponse = WikiResponse()
    ): GameResult()

    data class LoadNextArticleResult(
        val articleResponse: WikiResponse = WikiResponse()
    ): GameResult()

    object ShowVictoryScreenResult: GameResult()

    data class ToggleArticleModeResult(val type: ArticleType): GameResult()
}