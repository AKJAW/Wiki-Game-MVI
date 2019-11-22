package com.example.wikigamemvi.feature.game.model

import com.example.wikigamemvi.data.model.WikiArticle
import com.example.wikigamemvi.data.model.WikiResponse
import com.example.wikigamemvi.data.model.WikiUrl
import com.example.wikigamemvi.feature.base.BaseAction
import com.example.wikigamemvi.feature.base.BaseResult
import com.example.wikigamemvi.feature.base.BaseViewEffect
import com.example.wikigamemvi.feature.base.BaseViewState

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
    object ShowToastActionAAAA: GameAction()
    object LoadTargetArticleAction: GameAction()
}

sealed class GameResult: BaseResult {
    data class LoadTargetArticleResult(val articleResponse: WikiResponse): GameResult()
    data class ShowToastResult(val text: String): GameResult()
}