package com.akjaw.wikigamemvi.ui.game

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