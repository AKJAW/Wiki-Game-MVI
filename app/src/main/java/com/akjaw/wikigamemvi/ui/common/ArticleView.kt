package com.akjaw.wikigamemvi.ui.common

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import com.akjaw.domain.model.WikiArticle
import com.akjaw.wikigamemvi.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.article_collapsed.view.*
import kotlinx.android.synthetic.main.article_header.view.*
import kotlin.random.Random

class ArticleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {
    private var wikiArticle: WikiArticle? = null

    private var currentMode: Mode = Mode.COLLAPSED

    init {
        View.inflate(context, R.layout.article_expanded, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ArticleView)

        initializeFromAttributes(attributes)

        article_header_button_text_view.setOnClickListener {


        }
    }

    private fun initializeFromAttributes(attributes: TypedArray) {
        article_header_title_text_view.text = attributes.getString(R.styleable.ArticleView_header)

        val backgroundColor =
            attributes.getColor(R.styleable.ArticleView_headerBackgroundColor, Color.WHITE)

        setHeaderColor(backgroundColor)

        attributes.getString(R.styleable.ArticleView_titleTransitionName)?.let {
            article_title_text_view.transitionName = it
        }

        attributes.getString(R.styleable.ArticleView_imageTransitionName)?.let {
            article_image_view.transitionName = it
        }

        attributes.recycle()
    }

    fun setArticle(article: WikiArticle, shouldChangeHeaderColor: Boolean = false){
        if(article === wikiArticle){
            return
        }

        wikiArticle = article
        article_title_text_view.text = article.name

        if(article.imageUrl.isNotBlank()){
            Glide
                .with(context)
                .load(article.imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(article_image_view)
        }

        if(shouldChangeHeaderColor){
            val randomIndex = Random.nextInt(COLORS_ID.size)
            val id = COLORS_ID[randomIndex]
            val color = ResourcesCompat.getColor(resources, id, null)
            setHeaderColor(color)
        }
    }

    private fun setHeaderColor(color: Int){
        val headerBackground = header_background.background as GradientDrawable
        headerBackground.setColor(color)
    }

    fun setIsLoading(isLoading: Boolean){
        article_progress_bar.isVisible = isLoading
    }

    companion object {
        private val COLORS_ID = listOf(
            R.color.articleBlue,
            R.color.articlePurple,
            R.color.articleGreen
        )

        private enum class Mode{
            COLLAPSED,
            EXPANDED
        }
    }
}