package com.akjaw.wikigamemvi.ui.common.view

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.akjaw.domain.model.WikiArticle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.article_collapsed.view.*
import kotlinx.android.synthetic.main.article_header.view.*
import kotlin.random.Random
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.akjaw.wikigamemvi.R


class ArticleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {
    private var wikiArticle: WikiArticle? = null
    private var fullImageLoaded = false

    private var currentArticleViewMode: ArticleViewMode =
        ArticleViewMode.COLLAPSED

    private lateinit var collapsedConstraintSet: ConstraintSet
    private lateinit var expandedConstraintSet: ConstraintSet

    init {
        View.inflate(context, R.layout.article_collapsed, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ArticleView)

        initializeViewFromAttributes(attributes)
        initializeConstraintSets()
    }

    private fun initializeViewFromAttributes(attributes: TypedArray) {
        article_header_title_text_view.text = attributes.getString(R.styleable.ArticleView_header)

        val hasRandomHeaderColors = attributes
            .getBoolean(R.styleable.ArticleView_hasRandomHeaderColors, false)

        val backgroundColor = if(hasRandomHeaderColors){
            getRandomHeaderColor()
        } else {
            attributes.getColor(R.styleable.ArticleView_headerBackgroundColor, Color.WHITE)
        }


        setHeaderColor(backgroundColor)

        attributes.getString(R.styleable.ArticleView_titleTransitionName)?.let {
            article_title_text_view.transitionName = it
        }

        attributes.getString(R.styleable.ArticleView_imageTransitionName)?.let {
            article_image_view.transitionName = it
        }

        attributes.recycle()
    }

    private fun initializeConstraintSets() {
        collapsedConstraintSet = ConstraintSet().apply {
            clone(article_constraint_root)
        }

        expandedConstraintSet = ConstraintSet().apply {
            clone(context, R.layout.article_expanded)
        }
    }

    fun toggleView(newMode: ArticleViewMode) {
        if(newMode == currentArticleViewMode){
            return
        }

        val isExpanding = currentArticleViewMode == ArticleViewMode.COLLAPSED

        val constraintSet = when(isExpanding){
            true -> expandedConstraintSet
            false -> collapsedConstraintSet
        }

        val imgUrl = wikiArticle?.imageUrl

        if(imgUrl != null && !fullImageLoaded){
            downloadFullResolutionImage(imgUrl){
                runTransition(isExpanding)
                constraintSet.applyTo(article_constraint_root)
                fullImageLoaded = true
            }
        } else {
            runTransition(isExpanding)
            constraintSet.applyTo(article_constraint_root)
        }

        currentArticleViewMode = newMode
    }

    private fun runTransition(isExpanding: Boolean) {
        val transition = ChangeBounds()
        if(isExpanding){
            transition.interpolator = LinearOutSlowInInterpolator()
            transition.startDelay = 50
            transition.duration = 300
        } else {
            transition.interpolator = AccelerateInterpolator()
            transition.duration = 150
        }
        TransitionManager.beginDelayedTransition(article_constraint_root, transition)
    }

    private fun downloadFullResolutionImage(url: String, onLoadEnd: () -> Unit){
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                onLoadEnd()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                onLoadEnd()
                return false
            }
        }

        val circularProgressDrawable = CircularProgressDrawable(context).apply {
            strokeWidth = 5f
            centerRadius = 15f
            start()
        }

        Glide
            .with(context)
            .load(url)
            .listener(listener)
            .placeholder(circularProgressDrawable)
            .apply(RequestOptions()
                .override(Target.SIZE_ORIGINAL)
                .format(DecodeFormat.PREFER_ARGB_8888))
            .into(article_image_view)

    }

    fun setArticle(article: WikiArticle, shouldChangeHeaderColor: Boolean = false){
        if(article === wikiArticle){
            return
        }

        article_title_text_view.text = article.name

        if(article.imageUrl.isNotBlank()){
            Glide
                .with(context)
                .load(article.imageUrl)
                .into(article_image_view)
        }

        if(shouldChangeHeaderColor && wikiArticle != null){
            val color = getRandomHeaderColor()
            setHeaderColor(color)
        }

        wikiArticle = article
    }

    private fun getRandomHeaderColor(): Int {
        val randomIndex = Random.nextInt(COLORS_ID.size)
        val id = COLORS_ID[randomIndex]
        return ResourcesCompat.getColor(resources, id, null)
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
    }

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

}


