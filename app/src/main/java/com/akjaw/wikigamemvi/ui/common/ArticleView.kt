package com.akjaw.wikigamemvi.ui.common

import android.content.Context
import android.graphics.Color
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
    private lateinit var wikiArticle: WikiArticle

    init {
        View.inflate(context, R.layout.article, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ArticleView)

        article_header_text_view.text = attributes.getString(R.styleable.ArticleView_header)

        val backgroundColor = attributes.getColor(R.styleable.ArticleView_headerBackgroundColor, Color.WHITE)
        header_background.setBackgroundColor(backgroundColor)

        attributes.recycle()
    }

    fun setArticle(article: WikiArticle){
        wikiArticle = article
        article_title_text_view.text = article.name
    }

    fun setIsLoading(isLoading: Boolean){
        article_progress_bar.isVisible = isLoading
    }


}