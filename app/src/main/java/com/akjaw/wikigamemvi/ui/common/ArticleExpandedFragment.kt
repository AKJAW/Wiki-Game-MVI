package com.akjaw.wikigamemvi.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akjaw.domain.model.WikiArticle
import com.akjaw.wikigamemvi.R
import com.akjaw.wikigamemvi.ui.base.ParcelableWikiArticle
import kotlinx.android.synthetic.main.fragment_article_expanded.*


class ArticleExpandedFragment: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article_expanded, container, false)
    }
}