package com.akjaw.wikigamemvi.ui.game.model

import com.akjaw.domain.model.WikiArticle
import com.akjaw.domain.model.WikiResponse
import com.akjaw.domain.model.WikiTitle
import com.akjaw.wikigamemvi.ui.base.*
import com.akjaw.wikigamemvi.ui.common.ArticleView
import com.akjaw.wikigamemvi.ui.common.ArticleViewMode

data class GameViewState(
    val targetArticle: WikiArticle? = null,
    val isTargetArticleLoading: Boolean = false,
    val targetArticleMode: ArticleViewMode = ArticleViewMode.COLLAPSED,

    val currentArticle: WikiArticle? = null,
    val isCurrentArticleLoading: Boolean = false,
    val currentArticleMode: ArticleViewMode = ArticleViewMode.COLLAPSED,

    val numberOfSteps: Int = 0,
    val wikiNavigationLinks: List<WikiTitle> = listOf()
): BaseViewState

sealed class GameViewEffect: BaseViewEffect{
    data class SomeToastEffect(val text: String): GameViewEffect()
    object ShowVictoryScreenEffect: GameViewEffect()
}

sealed class GameAction: BaseAction {
    data class ShowToastAction(val text: String): GameAction()
    object InitializeArticlesAction: GameAction()
    data class LoadNextArticleAction(val wikiTitle: WikiTitle): GameAction()
    object ToggleTargetArticleModeAction: GameAction()
    object ToggleCurrentArticleModeAction: GameAction()
}

sealed class GameResult: BaseResult {
    data class ShowToastResult(val text: String): GameResult()

    data class InitializeArticlesResult(
        val targetArticleResponse: WikiResponse = WikiResponse(),
        val currentArticleResponse: WikiResponse = WikiResponse()
    ): GameResult()

    data class LoadNextArticleResult(
        val articleResponse: WikiResponse = WikiResponse()
    ): GameResult()

    object ShowVictoryScreenResult: GameResult()

    object ToggleTargetArticleModeResult: GameResult()
    object ToggleCurrentArticleModeResult: GameResult()
}