package com.akjaw.wikigamemvi.ui.common

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.akjaw.domain.model.WikiArticle
import com.akjaw.wikigamemvi.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlinx.android.synthetic.main.article_collapsed.view.*
import kotlinx.android.synthetic.main.article_header.view.*
import kotlin.random.Random

class ArticleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {
    private var wikiArticle: WikiArticle? = null

    private var currentMode: Mode = Mode.COLLAPSED

    init {
        View.inflate(context, R.layout.article_collapsed, this)

        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ArticleView)

        initializeFromAttributes(attributes)

        article_header_button_text_view.setOnClickListener(::onMoreClick)
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

    private fun onMoreClick(view: View) {
        val willBeExpanded = when(currentMode){
            Mode.COLLAPSED -> true
            Mode.EXPANDED -> false
        }

        val layout = if(willBeExpanded)  {
            R.layout.article_expanded
        } else {
            R.layout.article_collapsed
        }

        val constraintSet = ConstraintSet().apply {
            clone(context, layout)
        }

        val imgUrl = wikiArticle?.imageUrl

        val isThumbnail = !willBeExpanded

        if(imgUrl != null){
            updateImage(imgUrl, isThumbnail){
                runTransition()
                constraintSet.applyTo(article_constraint_root)
            }
        } else {
            runTransition()
            constraintSet.applyTo(article_constraint_root)
        }

        article_progress_bar.isVisible = false

        currentMode = when(willBeExpanded){
            true -> Mode.EXPANDED
            false -> Mode.COLLAPSED
        }
    }

    private fun runTransition() {
        val transition = ChangeBounds()
        transition.interpolator = FastOutSlowInInterpolator()
        TransitionManager.beginDelayedTransition(article_constraint_root, transition)
    }

    private fun updateImage(url: String, isThumbnail: Boolean, onImageLoad: () -> Unit){
        val listener = object : RequestListener<Drawable> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                onImageLoad()
                return false
            }

            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                onImageLoad()
                return false
            }
        }

        val glideLoad = Glide
            .with(context)
            .load(url)
            .listener(listener)


        val glideImage = if(isThumbnail) {
            glideLoad
                .apply(RequestOptions.circleCropTransform())
        } else {
            glideLoad
        }
        glideImage
            .apply(RequestOptions()
                .override(Target.SIZE_ORIGINAL)
                .format(DecodeFormat.PREFER_ARGB_8888))
            .into(article_image_view)
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