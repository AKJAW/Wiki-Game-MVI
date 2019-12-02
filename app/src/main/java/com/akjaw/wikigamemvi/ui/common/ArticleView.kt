package com.akjaw.wikigamemvi.ui.common

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
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

        article_header_title_text_view.text = attributes.getString(R.styleable.ArticleView_header)

        val backgroundColor = attributes.getColor(R.styleable.ArticleView_headerBackgroundColor, Color.WHITE)

        setHeaderColor(backgroundColor)
        attributes.recycle()
    }

    private fun setHeaderColor(color: Int){
        val headerBackground = header_background.background as GradientDrawable
        headerBackground.setColor(color)

    }

    fun setOnMoreClickListener(onClick: (View) -> Unit){
        article_header_button_text_view.setOnClickListener(onClick)
    }

    //TODO change header color?
    fun setArticle(article: WikiArticle){
        wikiArticle = article
        article_title_text_view.text = article.name
    }

    fun setIsLoading(isLoading: Boolean){
        article_progress_bar.isVisible = isLoading
    }


}