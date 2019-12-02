package com.akjaw.wikigamemvi.ui.common

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import com.akjaw.domain.model.WikiArticle
import com.akjaw.wikigamemvi.R
import kotlinx.android.synthetic.main.article.view.*

class ArticleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.article, this)
    }

    fun setArticle(article: WikiArticle){
        article_title_text_view.text = article.name
    }

    fun setIsLoading(isLoading: Boolean){
        article_progress_bar.isVisible = isLoading
    }
}