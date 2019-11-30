package com.akjaw.wikigamemvi.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.akjaw.domain.model.WikiArticle
import com.akjaw.wikigamemvi.R
import com.akjaw.wikigamemvi.ui.base.ParcelableWikiArticle
import kotlinx.android.synthetic.main.fragment_article.*
import java.lang.IllegalStateException


class ArticleFragment: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article, container, false).also {
            val article = arguments?.getParcelable<ParcelableWikiArticle>(EXTRA_ARTICLE)

            if(article != null){
//                article_image.text =
                article_title.text = article.description
                article_description.text = article.description
            }

        }
    }

    companion object {
        const val SHARED_TRANSITION_TITLE = "title"
        const val EXTRA_ARTICLE = "EXTRA_ARTICLE"

        fun newInstance(article: WikiArticle): ArticleFragment{
            val parcelableArticle = ParcelableWikiArticle(
                article.name,
                article.description,
                article.imageUrl
            )
            val fragment = ArticleFragment()

            val args = Bundle()
            args.putParcelable(EXTRA_ARTICLE, parcelableArticle)
            fragment.arguments = args

            return fragment
        }
    }
}