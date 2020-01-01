package com.akjaw.wikigamemvi.ui.game

import com.akjaw.domain.model.ArticleType
import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import com.akjaw.wikigamemvi.ui.base.BaseAction
import com.akjaw.wikigamemvi.ui.base.BaseResult
import com.akjaw.wikigamemvi.ui.base.BaseViewEffect
import com.akjaw.wikigamemvi.ui.base.BaseViewState
import com.akjaw.wikigamemvi.ui.common.view.ArticleView

data class ArticleState(
    val article: WikiArticle? = null,
    val isLoading: Boolean = false,
    val mode: ArticleView.ArticleViewMode = ArticleView.ArticleViewMode.COLLAPSED,
    val hasError: Boolean = false
)

data class GameViewState(
    val targetArticleState: ArticleState = ArticleState(),
    val currentArticleState: ArticleState = ArticleState(),
    val numberOfSteps: Int = 0,
    val wikiNavigationLinks: List<WikiTitle> = listOf()
): BaseViewState

sealed class GameViewEffect: BaseViewEffect {
    data class ShowVictoryScreenEffect(
        val article: WikiArticle,
        val numberOfSteps: Int
    ): GameViewEffect()
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