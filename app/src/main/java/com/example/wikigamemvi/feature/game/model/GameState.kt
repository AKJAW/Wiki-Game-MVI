package com.example.wikigamemvi.feature.game.model

import com.example.wikigamemvi.data.model.WikiArticle
import com.example.wikigamemvi.data.model.WikiUrl
import com.example.wikigamemvi.feature.base.BaseAction
import com.example.wikigamemvi.feature.base.BaseViewEffect
import com.example.wikigamemvi.feature.base.BaseViewState

data class GameViewState(
    val targetArticle: WikiArticle,
    val isTargetArticleLoading: Boolean,
    val currentArticle: WikiArticle,
    val isCurrentArticleLoading: Boolean,
    val navigationUrls: List<WikiUrl>
): BaseViewState

sealed class GameViewEffect: BaseViewEffect{
    object someToast: GameViewEffect()
}

sealed class GameAction: BaseAction {
    object loadTargetArticle: GameAction()
}