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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.akjaw.domain.model.WikiArticle
import com.akjaw.wikigamemvi.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.article.view.*
import kotlinx.android.synthetic.main.fragment_article_collapsed.view.*
import kotlin.random.Random

class ArticleView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : CardView(context, attrs, defStyleAttr) {

    private var wikiArticle: WikiArticle? = null
    private var currentMode: Mode = Mode.COLLAPSED

    init {
        View.inflate(context, R.layout.article, this)

//        inflateCollapsedFragment(context)

        //TODO
//        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ArticleView)
//        initializeWithAttributes(attributes)
    }

    fun replaceFragment(context: Context, fragmentManager: FragmentManager) {
        (context as Fragment)

        val transition = fragmentManager.beginTransaction()

        //TODO
//        transition.addSharedElement(target_article_view.article_image_view, imageTransitionName)

        val fragment = when(currentMode){
            Mode.COLLAPSED -> ArticleCollapsedFragment()
            Mode.EXPANDED -> ArticleExpandedFragment()
        }



        transition.replace(R.id.main_fragment_placeholder, fragment)
        transition.commit()
    }

    private fun initializeWithAttributes(attributes: TypedArray) {

        article_header_title_text_view.text = attributes.getString(R.styleable.ArticleView_header)

        val backgroundColor =
            attributes.getColor(R.styleable.ArticleView_headerBackgroundColor, Color.WHITE)

        setHeaderColor(backgroundColor)

        attributes.getString(R.styleable.ArticleView_titleTransitionName)?.let {
            collapsed_title_text_view.transitionName = it
        }

        attributes.getString(R.styleable.ArticleView_imageTransitionName)?.let {
            collapsed_image_view.transitionName = it
        }

        attributes.recycle()
    }

    fun setOnMoreClickListener(onClick: (View) -> Unit){
        article_header_button_text_view.setOnClickListener(onClick)
    }

    fun setArticle(article: WikiArticle, shouldChangeHeaderColor: Boolean = false){
        if(article === wikiArticle){
            return
        }

        wikiArticle = article
        collapsed_title_text_view.text = article.name

        if(article.imageUrl.isNotBlank()){
            Glide
                .with(context)
                .load(article.imageUrl)
                .apply(RequestOptions.circleCropTransform())
                .into(collapsed_image_view)
        } else {
            //TODO palceholder
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
        collapsed_progress_bar.isVisible = isLoading
    }

    fun getSharedImageView(){
        return when(currentMode){
            Mode.COLLAPSED -> TODO()
            Mode.EXPANDED -> TODO()
        }
    }

    companion object {
        private val COLORS_ID = listOf(
            R.color.articleBlue,
            R.color.articlePurple,
            R.color.articleGreen
        )

        enum class Mode{
            COLLAPSED,
            EXPANDED
        }
    }
}