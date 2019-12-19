package com.akjaw.presentation.game

enum class ArticleViewMode{
    COLLAPSED,
    EXPANDED;

    fun inverted(): ArticleViewMode {
        return when (this) {
            COLLAPSED -> EXPANDED
            EXPANDED -> COLLAPSED
        }
    }
}